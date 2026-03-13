package com.example.loganalyzer.service;

import com.example.loganalyzer.model.LogEntry;
import com.example.loganalyzer.model.LogLevel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface LogAnalyzer {

    Map<LogLevel, Integer> getLogSummary();

    List<LogEntry> getErrorLogs();

    String getMostFrequentErrorMessage();

    List<LogEntry> filterLogsByLevel(LogLevel logLevel);

    List<LogEntry> searchLogByKeyword(String keyword);

    List<LogEntry> filterLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<LogEntry> getLogsSortedByTimestamp();

    double getErrorPercentage();

    Map<String, Integer> getErrorFrequencyTimeline();

    String generateSystemReport();
}
