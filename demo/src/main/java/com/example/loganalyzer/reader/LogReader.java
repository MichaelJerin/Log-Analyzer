package com.example.loganalyzer.reader;

import com.example.loganalyzer.model.LogEntry;

public interface LogReader {

    void readLogs(String filePath);

    LogEntry parseLine(String line);
}
