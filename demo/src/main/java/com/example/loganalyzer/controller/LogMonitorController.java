package com.example.loganalyzer.controller;

import com.example.loganalyzer.monitor.LogMonitorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
public class LogMonitorController {

    private final LogMonitorService logMonitorService;

    public LogMonitorController(LogMonitorService logMonitorService) {
        this.logMonitorService =  logMonitorService;
    }

    @PostMapping("/start")
    public String startMonitoring(@RequestParam String path) {
        logMonitorService.monitorLogFile(path);
        return "Monitoring started for: " + path;
    }

    @PostMapping("/stop")
    public String stopMonitoring() {
        logMonitorService.stopMonitoring();
        return "Monitoring stopped.";
    }
}
