package com.example.loganalyzer.util;

import com.example.loganalyzer.model.LogEntry;

import java.util.ArrayList;
import java.util.List;

public class InMemoryLogStore implements LogStore{

    private static InMemoryLogStore instance;
    private List<LogEntry> logs;

    private InMemoryLogStore() {
        logs = new ArrayList<>();
    }

    public static InMemoryLogStore getInstance() {
        if(instance == null) {
            instance = new InMemoryLogStore();
        }
        return instance;
    }

    @Override
    public List<LogEntry> getAllLogs() {
        return logs;
    }

    @Override
    public void addLog(LogEntry log) {
        logs.add(log);
    }

    @Override
    public void clear() {
        logs.clear();
    }
}
