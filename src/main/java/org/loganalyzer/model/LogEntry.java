package org.loganalyzer.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LogEntry {

    private LocalDateTime timestamp;
    private LogLevel level;
    private String message;

    public LogEntry(LocalDateTime timestamp, LogLevel level, String message) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
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

    @Override
    public String toString() {
        return timestamp + " " + level + " " +  message;
    }
}
