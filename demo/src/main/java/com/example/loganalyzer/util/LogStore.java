package com.example.loganalyzer.util;

import com.example.loganalyzer.model.LogEntry;

import java.util.List;

public interface LogStore {

    void addLog(LogEntry log);

    List<LogEntry> getAllLogs();

    void clear();
}
