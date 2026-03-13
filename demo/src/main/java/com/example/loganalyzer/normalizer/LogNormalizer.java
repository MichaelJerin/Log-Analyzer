package com.example.loganalyzer.normalizer;

import com.example.loganalyzer.model.LogEntry;

public interface LogNormalizer {

    LogEntry normalize (LogEntry entry);

}
