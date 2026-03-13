package com.example.loganalyzer.reader;

import com.example.loganalyzer.model.LogEntry;
import com.example.loganalyzer.model.LogLevel;
import com.example.loganalyzer.model.LogSource;
import com.example.loganalyzer.normalizer.LogNormalizer;
import com.example.loganalyzer.normalizer.LogNormalizerImpl;
import com.example.loganalyzer.util.InMemoryLogStore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogReaderImpl implements LogReader {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LogNormalizer normalizer = new LogNormalizerImpl();

    @Override
    public void readLogs(String filePath) {

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {

            String line;

            while((line = bufferedReader.readLine()) != null) {

                LogEntry entry = parseLine(line);

                if(entry != null) {
                    LogEntry normalizedEntry = normalizer.normalize(entry);
                    InMemoryLogStore.getInstance().addLog(normalizedEntry);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file : " + e.getMessage());
        }
    }

    @Override
    public LogEntry parseLine(String line) {

        try {
            String[] parts = line.split(" ", 4);

            String date = parts[0];
            String time = parts[1];
            String level = parts[2];
            String message = parts[3];

            LocalDateTime timeStamp = LocalDateTime.parse(date + " " + time, FORMATTER);
            LogLevel logLevel = LogLevel.valueOf(level);

            LogEntry entry = new LogEntry(timeStamp, logLevel, message);
            entry.setSource(LogSource.FILE);

            return entry;

        } catch (Exception e) {
            System.out.println("Skipping invalid log : " + line);
            return null;
        }
    }
}
