package org.loganalyzer.reader;

import org.loganalyzer.model.LogEntry;
import org.loganalyzer.model.LogLevel;
import org.loganalyzer.util.LogStorage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogReaderImpl implements LogReader {

    private static final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void readLogs(String filePath) {

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {

            String line;

            while((line = bufferedReader.readLine()) != null) {

                LogEntry entry = parseLine(line);

                if(entry != null) {
                    LogStorage.getInstance().add(entry);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file : " + e.getMessage());
        }
    }

    private LogEntry parseLine(String line) {

        try {
            String[] parts = line.split(" ", 4);

            String date = parts[0];
            String time = parts[1];
            String level = parts[2];
            String message = parts[3];

            LocalDateTime timeStamp = LocalDateTime.parse(date + " " + time, Formatter);

            LogLevel logLevel = LogLevel.valueOf(level);

            return new LogEntry(timeStamp, logLevel, message);

        } catch (Exception e) {
            System.out.println("Skipping invalid log : " + line);
            return null;
        }
    }
}
