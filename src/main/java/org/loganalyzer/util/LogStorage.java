package org.loganalyzer.util;

import org.loganalyzer.model.LogEntry;

import java.util.ArrayList;
import java.util.List;

public class LogStorage {

    private static LogStorage instance;
    private List<LogEntry> logs;

    private LogStorage() {
        logs = new ArrayList<>();
    }

    public static LogStorage getInstance() {
        if(instance == null) {
            instance = new LogStorage();
        }
        return instance;
    }

    public void add(LogEntry log) {
        logs.add(log);
    }

    public List<LogEntry> getAllLogs(){
        return logs;
    }
}
