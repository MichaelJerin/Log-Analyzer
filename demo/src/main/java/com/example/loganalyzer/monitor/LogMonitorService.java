package com.example.loganalyzer.monitor;

public interface LogMonitorService {

    void monitorLogFile(String path);

    void stopMonitoring();
}

