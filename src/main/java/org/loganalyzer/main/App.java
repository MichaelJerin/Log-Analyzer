package org.loganalyzer.main;

import org.loganalyzer.model.LogEntry;
import org.loganalyzer.model.LogLevel;
import org.loganalyzer.service.LogAnalyzer;
import org.loganalyzer.service.LogAnalyzerImpl;
import org.loganalyzer.reader.LogReader;
import org.loganalyzer.reader.LogReaderImpl;
import org.loganalyzer.util.LogStorage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.print("Enter log file path : ");
        String path = scanner.nextLine();

        String[] paths = path.split(",");

        try {
            LogReader logReader = new LogReaderImpl();
            for(String p : paths) {
                logReader.readLogs(p.trim());
            }
            System.out.println("Logs loaded successfully.");
        } catch (Exception e) {
            System.out.println("Failed to read log file");
             return;
        }

        LogAnalyzer logAnalyzer = new LogAnalyzerImpl();

        while(true) {

            System.out.println("      LOG ANALYZER       ");
            System.out.println("=========================");
            System.out.println("1. View all Logs");
            System.out.println("2. Show log summary");
            System.out.println("3. Show error logs");
            System.out.println("4. Search Logs by keyword");
            System.out.println("5. Search logs by date range");
            System.out.println("6. Sort logs by times");
            System.out.println("7. Calculate error percentage");
            System.out.println("8. Error frequency timeline");
            System.out.println("9. Generate system report");
            System.out.println("10. Exit");
            System.out.println("=========================");
            System.out.println();

            System.out.print("Enter choice : ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    viewAllLogs();
                    System.out.println();
                    break;

                case 2:
                    showSummary(logAnalyzer);
                    System.out.println();
                    break;

                case 3:
                    showErrorLog(logAnalyzer);
                    System.out.println();
                    break;

                case 4:
                    searchLogs(logAnalyzer);
                    System.out.println();
                    break;

                case 5:
                    searchLogsByDate(logAnalyzer);
                    System.out.println();
                    break;

                case 6:
                    sortLogByTime(logAnalyzer);
                    System.out.println();
                    break;

                case 7:
                    calculateErrorPercentage(logAnalyzer);
                    System.out.println();
                    break;

                case 8:
                    errorFrequencyTimeLine(logAnalyzer);
                    System.out.println();
                    break;

                case 9:
                    System.out.println(logAnalyzer.generateSystemReport());
                    break;

                case 10:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice. Try again");
            }
        }
    }

    private static void viewAllLogs() {

        List<LogEntry> allLogs = LogStorage.getInstance().getAllLogs();

        if(allLogs.isEmpty()) {
            System.out.println("No Logs found.");
            return;
        }

        System.out.println("\n====== View All Log =======\n");

        for (LogEntry log : allLogs) {
            System.out.println(log.getTimestamp() + " " + log.getLevel() + " " + log.getMessage());
        }
    }

    private static void showSummary(LogAnalyzer logAnalyzer) {

        Map<LogLevel, Integer> summary = logAnalyzer.getLogSummary();

        System.out.println("\n==== View log summary =====\n");

        for(LogLevel level : LogLevel.values()) {
            System.out.println("Level: " + level + " Count: " + summary.getOrDefault(level, 0));
        }
    }

    private static void showErrorLog(LogAnalyzer logAnalyzer) {

        List<LogEntry> errors = logAnalyzer.getErrorLogs();

        if(errors.isEmpty()) {
            System.out.println("No error log found.");
            return;
        }

        System.out.println("\n===== Show error Logs ======\n");

        for (LogEntry error : errors) {
            System.out.println(error);
        }
        System.out.println();
        System.out.println("Most Frequent Error : " + logAnalyzer.getMostFrequentErrorMessage());
    }

    private static void searchLogs(LogAnalyzer logAnalyzer) {

        System.out.print("Enter Keyword to search : ");
        String keyword = scanner.nextLine();

        List<LogEntry> logs = logAnalyzer.searchLogByKeyword(keyword);

        System.out.println("\n===== Search log by keywords ======\n");

        if(logs.isEmpty()) {
            System.out.println("No log found with keyword : " + keyword);
        } else {
            for (LogEntry log : logs) {
                System.out.println(log);
            }
        }

    }

    private static void searchLogsByDate(LogAnalyzer logAnalyzer) {

        System.out.print("Enter start date time (YYYY-MM-DD HH:MM:SS) : ");
        String startDate = scanner.nextLine();

        System.out.print("Enter end date time (YYYY-MM-DD HH:MM:SS) : ");
        String endDate = scanner.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);

        List<LogEntry> logs = logAnalyzer.filterLogsByDateRange(start, end);

        System.out.println("\n===== Search log by date range ======\n");

        if(logs.isEmpty()) {
            System.out.println("No log found in the give date range.");
        } else {
            for (LogEntry log : logs) {
                System.out.println(log);
            }
        }
    }

    private static void sortLogByTime(LogAnalyzer logAnalyzer) {

        List<LogEntry> sortedLogs = logAnalyzer.getLogsSortedByTimestamp();

        System.out.println("\n===== Logs sorted by timestamp ======\n");

        if (sortedLogs.isEmpty()) {
            System.out.println("No logs found.");
        } else {
            for (LogEntry log : sortedLogs) {
                System.out.println(log);
            }
        }
    }

    private static void calculateErrorPercentage(LogAnalyzer logAnalyzer) {

        System.out.print("\n===== Error percentage ======\n");
        System.out.printf("Error Percentage : %.2f%%\n", logAnalyzer.getErrorPercentage());
    }

    private static void errorFrequencyTimeLine(LogAnalyzer logAnalyzer) {

        Map<String, Integer> timeline = logAnalyzer.getErrorFrequencyTimeline();

        System.out.println("\n===== Error frequency timeline =====\n");

        if(timeline.isEmpty()) {
            System.out.println("No error logs found.");
        } else {
            for (Map.Entry<String, Integer> entry : timeline.entrySet()) {
                System.out.println(entry.getKey()  + " : " + entry.getValue());
            }
        }
    }
}
