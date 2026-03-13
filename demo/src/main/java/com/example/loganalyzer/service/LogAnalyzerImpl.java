package com.example.loganalyzer.service;

import org.springframework.stereotype.Service;

import com.example.loganalyzer.model.LogEntry;
import com.example.loganalyzer.model.LogLevel;
import com.example.loganalyzer.util.InMemoryLogStore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class LogAnalyzerImpl implements LogAnalyzer {

    private List<LogEntry> logs;

    public LogAnalyzerImpl() {
        logs = InMemoryLogStore.getInstance().getAllLogs();
    }

    @Override
    public Map<LogLevel, Integer> getLogSummary() {

        Map<LogLevel,Integer> summary = new HashMap<>();
        summary.put(LogLevel.INFO, 0);
        summary.put(LogLevel.WARN, 0);
        summary.put(LogLevel.ERROR, 0);

        for(LogEntry log : logs) {
            LogLevel level = log.getLevel();
            summary.put(level, summary.get(level) + 1);
        }

        return summary;
    }

    @Override
    public List<LogEntry> getErrorLogs() {
        return filterLogsByLevel(LogLevel.ERROR);
    }

    @Override
    public String getMostFrequentErrorMessage() {

        Map<String, Integer> frequency = new HashMap<>();

        for(LogEntry log : logs) {
            if(log.getLevel() == LogLevel.ERROR){
                String message = log.getMessage();
                frequency.put(message, frequency.getOrDefault(message, 0) + 1);
            }
        }

        if(frequency.isEmpty()) {
            return "No error log found.";
        }

        String mostFrequent = null;
        int max = 0;

        for (Map.Entry<String, Integer> entry : frequency.entrySet()) {
            if(entry.getValue() > max) {
                max = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }

        return mostFrequent != null ? mostFrequent + " (occurred " + max + " times)" : "No error log found.";
    }

    @Override
    public List<LogEntry> filterLogsByLevel(LogLevel logLevel) {

        List<LogEntry> filteredLogs = new ArrayList<>();

        for(LogEntry log : logs) {
            if(log.getLevel() == logLevel) {
                filteredLogs.add(log);
            }
        }

        return filteredLogs;
    }

    @Override
    public List<LogEntry> searchLogByKeyword(String keyword) {

        List<LogEntry> list = new ArrayList<>();

        for (LogEntry log : logs) {
            if(log.getMessage().toLowerCase().contains(keyword.toLowerCase())) {
                list.add(log);
            }
        }

        return list;
    }

    @Override
    public List<LogEntry> filterLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {

        List<LogEntry> filteredLogs = new ArrayList<>();

        for (LogEntry log : logs) {
            LocalDateTime timestamp = log.getTimestamp();

            if ((timestamp.isEqual(startDate) || timestamp.isAfter(startDate)) &&
                    (timestamp.isEqual(endDate) || timestamp.isBefore(endDate))) {
                filteredLogs.add(log);
            }
        }

        return filteredLogs;
    }

    @Override
    public List<LogEntry> getLogsSortedByTimestamp() {

        List<LogEntry> sortedLogs = new ArrayList<>(logs);

        sortedLogs.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));

        return sortedLogs;
    }

    @Override
    public double getErrorPercentage() {

        if(logs.isEmpty()) {
            return 0.0;
        }

        int errorCount = 0;

        for(LogEntry log : logs) {
            if(log.getLevel() == LogLevel.ERROR) {
                errorCount++;
            }
        }

        return (errorCount * 100.0) / logs.size();
    }

    @Override
    public Map<String, Integer> getErrorFrequencyTimeline() {

        Map<String, Integer> timeline = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (LogEntry log : logs) {
            if(log.getLevel() == LogLevel.ERROR) {
                String time = log.getTimestamp().format(formatter);
                timeline.put(time, timeline.getOrDefault(time, 0) + 1);
            }
        }

        return timeline;
    }

    @Override
    public String generateSystemReport() {

        Map<LogLevel, Integer> summary = getLogSummary();
        double errorPercentage = getErrorPercentage();
        String mostFrequentError = getMostFrequentErrorMessage();

        int totalLogs = logs.size();

        String status = errorPercentage > 30 ? "UNSTABLE" : "STABLE";

        StringBuilder report = new StringBuilder();

        System.out.print("\n===== System Report =====");

        report.append("Total Logs : ").append(totalLogs).append("\n\n");

        for(LogLevel level : LogLevel.values()) {
            report.append("Level: " + level + " Count: " + summary.getOrDefault(level, 0)).append("\n");
        }
        report.append("\n");

        report.append(String.format("Error Percentage : %.2f%%\n", errorPercentage)).append("\n");

        report.append("Most Frequent Error : \n");
        report.append(mostFrequentError != null ? mostFrequentError : "None").append("\n\n");
        System.out.println();

        report.append("System Status : ").append(status).append("\n");

        return report.toString();
    }
}
