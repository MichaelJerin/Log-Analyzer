package com.example.loganalyzer.syslog;

import com.example.loganalyzer.model.LogEntry;
import com.example.loganalyzer.model.LogLevel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class SyslogParser {

    private static final DateTimeFormatter SYSLOG_FORMATTER = DateTimeFormatter.ofPattern("yyyy MMM dd HH:mm:SS", Locale.ENGLISH);

    public LogEntry parse(String rawMessage, String senderIP) {

        if(rawMessage == null || rawMessage.isBlank()) {
            return null;
        }

        try {
            String message = rawMessage.trim();
            LogLevel level = LogLevel.INFO;
            LocalDateTime timestamp = LocalDateTime.now();

            // Extract priority if present e.g. <34>
            if(message.startsWith("<")) {
                int closeBracket = message.indexOf('>');
                if(closeBracket > 0) {
                    String priorityStr = message.substring(1, closeBracket);
                    int priority = Integer.parseInt(priorityStr);
                    int severity = priority & 0x07;
                    level = mapServerityToLogLevel(severity);
                    message = message.substring(closeBracket + 1).trim();
                }
            }

            // Extract timestamp (first 15 chars: "Jan 10 10:23:01")
            if(message.length() > 15) {
                String timestampStr = message.substring(0, 15);
                try {
                    int currentYear = LocalDateTime.now().getYear();
                    timestamp = LocalDateTime.parse(currentYear + " " + timestampStr, SYSLOG_FORMATTER);
                    message = message.substring(15).trim();
                } catch (DateTimeParseException e) {
                    timestamp = LocalDateTime.now();
                }
            }

            // Skip hostname (next word after timestamp)
            int firstSpace = message.indexOf(' ');
            if(firstSpace > 0) {
                message = message.substring(firstSpace + 1).trim();
            }

            String finalMessage = "[" + senderIP + "] " + message;

            return new LogEntry(timestamp, level, finalMessage);

        } catch (Exception e) {
            System.err.println("Failed to parse syslog message : " + rawMessage +
                    " | Error : " + e.getMessage());
            return null;
        }
    }

    private LogLevel mapServerityToLogLevel(int severity) {
        if(severity <= 3) return LogLevel.ERROR;
        else if(severity == 4) return LogLevel.WARN;
        return LogLevel.INFO;
    }
}
