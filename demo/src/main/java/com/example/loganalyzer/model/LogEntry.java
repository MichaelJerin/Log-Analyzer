package com.example.loganalyzer.model;

import java.time.LocalDateTime;

public class LogEntry {

    private LocalDateTime timestamp;
    private LogLevel level;
    private String message;
    private LogSource source; // Where did this log come from? (File, Syslog, API)
    private LogCategory category; // What type of event? (Auth, Network, etc.)
    private String sourceIp;
    private String hostName;

    // Constructor used by LogReaderImpl (file based logs)
    public LogEntry(LocalDateTime timestamp, LogLevel level, String message) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.source = LogSource.FILE;
        this.category = LogCategory.UNKNOWN;
        this.sourceIp = "unknown";
        this.hostName = "unknown";
    }

    public LogEntry(LocalDateTime timestamp, LogLevel level, String message, LogSource source, LogCategory category, String sourceIp, String hostName) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.source = source;
        this.category = category;
        this.sourceIp = sourceIp;
        this.hostName = hostName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LogSource getSource() {
        return source;
    }

    public void setSource(LogSource source) {
        this.source = source;
    }

    public LogCategory getCategory() {
        return category;
    }

    public void setCategory(LogCategory category) {
        this.category = category;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                timestamp + " [" + level + "] [" + source + "] [" + category + "] host = "
                + hostName + " IP = " + sourceIp + " | " + message;
    }
}
