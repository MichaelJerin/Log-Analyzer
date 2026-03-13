package com.example.loganalyzer.controller;

import com.example.loganalyzer.model.LogEntry;
import com.example.loganalyzer.reader.LogReader;
import com.example.loganalyzer.reader.LogReaderImpl;
import com.example.loganalyzer.service.LogAnalyzer;
import com.example.loganalyzer.util.InMemoryLogStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private final LogAnalyzer logAnalyzer;

    public LogController(LogAnalyzer logAnalyzer) {
        this.logAnalyzer = logAnalyzer;
    }

    @PostMapping("/upload")
    public String uploadLogs(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("upload", ".log");
            file.transferTo(tempFile);

            LogReader logReader = new LogReaderImpl();
            logReader.readLogs(tempFile.getAbsolutePath());

            return "Logs uploaded and loaded successfully.";
        } catch (Exception e) {
            return "Failed to upload file : " + e.getMessage();
        }
    }



    @PostMapping("/load")
    public String loadLogs(String path) {
        try {
            LogReader logReader = new LogReaderImpl();
            logReader.readLogs(path);
            return "Logs loaded successfully.";
        } catch (Exception e) {
            return "Failed to load logs";
        }
    }

    @GetMapping("/all-logs")
    public List<LogEntry> getAllLogs() {
        return InMemoryLogStore.getInstance().getAllLogs();
    }

    @GetMapping("/summary")
    public Map<?, ?> getSummary() {
        return logAnalyzer.getLogSummary();
    }

    @GetMapping("/error-logs")
    public List<LogEntry> getErrorLogs() {
        return logAnalyzer.getErrorLogs();
    }

    @GetMapping("/search-logs-keyword")
    public List<LogEntry> searchLogsByKeyword(@RequestParam String keyword) {
        return logAnalyzer.searchLogByKeyword(keyword);
    }

    @GetMapping("/search-logs-by-daterange")
    public List<LogEntry> searchLogsByDateRange(@RequestParam String startDate, @RequestParam String endDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);

        return logAnalyzer.filterLogsByDateRange(start, end);
    }

    @GetMapping("/sortedlist")
    public List<LogEntry> getLogsSortedByTimestamp() {
        return logAnalyzer.getLogsSortedByTimestamp();
    }

    @GetMapping("/error-percentage")
    public double getErrorPercentage() {
        return logAnalyzer.getErrorPercentage();
    }

    @GetMapping("/timeline")
    public Map<String,Integer> getErrorTimeline() {
        return logAnalyzer.getErrorFrequencyTimeline();
    }

    @GetMapping("/report")
    public String generateReport() {
        return logAnalyzer.generateSystemReport();
    }
}
