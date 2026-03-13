package com.example.loganalyzer.normalizer;

import com.example.loganalyzer.model.LogCategory;
import com.example.loganalyzer.model.LogEntry;
import com.example.loganalyzer.model.LogSource;

public class SyslogNormalizer implements LogNormalizer {

    @Override
    public LogEntry normalize(LogEntry entry) {

        entry.setSource(LogSource.SYSLOG);

        String message = entry.getMessage();

        String senderIP = extractSenderIP(message);
        entry.setSourceIp(senderIP != null ? senderIP : "unknown");

        String cleanMessage = removeSenderIPPrefix(message);

        String hostName = extractHostName(cleanMessage);
        entry.setHostName(hostName != null ? hostName : "unknown");

        entry.setCategory(determineCategory(cleanMessage.toLowerCase()));

        return entry;
    }

    private String extractSenderIP(String message) {
        if(message.startsWith("[")) {
            int closingBracket = message.indexOf(']');
            if(closingBracket > 0) {
                return message.substring(1, closingBracket);
            }
        }
        return null;
    }

    private String removeSenderIPPrefix(String message) {
        if(message.startsWith("[")) {
            int closingBracket = message.indexOf(']');
            if(closingBracket > 0) {
                return message.substring(closingBracket + 2);
            }
        }
        return message;
    }

    private String extractHostName(String message) {
        if(message == null || message.isBlank()) return null;
        String[] parts = message.split("\\s+");
        return parts.length > 0 ? parts[0] : null;
    }

    private LogCategory determineCategory(String message) {

        // Auth keywords
        if(message.contains("sshd") || message.contains("password")
                || message.contains("login") || message.contains("logout")
                || message.contains("authentication") || message.contains("auth")
                || message.contains("sudo") || message.contains("su ")
                || message.contains("pam") || message.contains("session opened")
                || message.contains("session closed") || message.contains("invalid user")) {
            return LogCategory.AUTHENTICATION;
        }

        // Network keywords
        if(message.contains("iptables") || message.contains("firewall")
                || message.contains("blocked") || message.contains("denied")
                || message.contains("port") || message.contains("connection")
                || message.contains("interface") || message.contains("routing")
                || message.contains("dhcp") || message.contains("dns")) {
            return LogCategory.NETWORK;
        }

        // System keywords
        if(message.contains("kernel") || message.contains("shutdown")
                || message.contains("reboot") || message.contains("started")
                || message.contains("stopped") || message.contains("failed")
                || message.contains("systemd") || message.contains("disk")
                || message.contains("mount") || message.contains("cron")) {
            return LogCategory.SYSTEM;
        }

        // Database keyword
        if(message.contains("mysql") || message.contains("postgres")
                || message.contains("mongodb") || message.contains("database")) {
            return LogCategory.DATABASE;
        }

        // Application keyword
        if(message.contains("error") || message.contains("exception")
                || message.contains("crash") || message.contains("core dump")) {
            return LogCategory.APPLICATION;
        }

        return LogCategory.UNKNOWN;
    }
}
