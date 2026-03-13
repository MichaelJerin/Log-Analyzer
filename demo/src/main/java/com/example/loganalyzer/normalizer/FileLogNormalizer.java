package com.example.loganalyzer.normalizer;

import com.example.loganalyzer.model.LogCategory;
import com.example.loganalyzer.model.LogEntry;
import com.example.loganalyzer.model.LogSource;
import org.apache.commons.logging.Log;

public class FileLogNormalizer implements LogNormalizer {

    @Override
    public LogEntry normalize(LogEntry entry) {

        entry.setSource(LogSource.FILE);
        entry.setHostName("localhost");;

        String message = entry.getMessage().toLowerCase();

        String extractIP = extractIP(entry.getMessage());
        entry.setSourceIp(extractIP != null ? extractIP : "unknown");

        entry.setCategory(determineCategory(message));

        return entry;
    }

    private String extractIP(String message) {

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "\\b(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\b"
        );
        java.util.regex.Matcher matcher = pattern.matcher(message);
        return matcher.find() ? matcher.group() : null;

    }

    private LogCategory determineCategory(String message) {

        // Auth related keywords
        if(message.contains("login") || message.contains("logout")
                || message.contains("password") || message.contains("authentication")
                || message.contains("auth") || message.contains("session")
                || message.contains("signin") || message.contains("signout")
                || message.contains("credential") || message.contains("token")) {
            return LogCategory.AUTHENTICATION;
        }

        // Network related keywords
        if(message.contains("connection") || message.contains("firewall")
                || message.contains("port") || message.contains("network")
                || message.contains("denied") || message.contains("blocked")
                || message.contains("refused") || message.contains("timeout")
                || message.contains("socket") || message.contains("tcp")
                || message.contains("udp") || message.contains("ssh")) {
            return LogCategory.NETWORK;
        }

        // Database related keywords
        if(message.contains("database") || message.contains("query")
                || message.contains("sql") || message.contains("db")
                || message.contains("transaction") || message.contains("deadlock")
                || message.contains("jdbc") || message.contains("datasource")) {
            return LogCategory.DATABASE;
        }

        // System-level keywords
        if(message.contains("shutdown") || message.contains("reboot")
                || message.contains("startup") || message.contains("kernel")
                || message.contains("disk") || message.contains("memory")
                || message.contains("cpu") || message.contains("system")) {
            return LogCategory.SYSTEM;
        }

        // Application-level keywords
        if(message.contains("exception") || message.contains("error")
                || message.contains("failed") || message.contains("crash")
                || message.contains("service") || message.contains("request")
                || message.contains("response") || message.contains("null")) {
            return LogCategory.APPLICATION;
        }

        return LogCategory.UNKNOWN;
    }

}
