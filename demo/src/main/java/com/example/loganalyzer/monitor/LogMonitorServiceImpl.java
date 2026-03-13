package com.example.loganalyzer.monitor;

import com.example.loganalyzer.model.LogEntry;
import com.example.loganalyzer.reader.LogReader;
import com.example.loganalyzer.reader.LogReaderImpl;
import com.example.loganalyzer.util.InMemoryLogStore;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class LogMonitorServiceImpl implements LogMonitorService {

    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread monitorThread;

    @Override
    public void monitorLogFile(String path) {

        //Prevent duplicate monitor threads for the same instance
        if(running.get()) {
            System.out.println("Monitor is already running.");
            return;
        }

        running.set(true);

        LogReader reader = new LogReaderImpl();

        monitorThread = new Thread( () -> {

            File file = new File(path);

            if(!file.exists() || !file.isFile()) {
                System.err.println("Log file does not exists : " + path);
                running.set(false);
                return;
            }

            long lastPosition = file.length();

            while(running.get()) {
                try {
                    long fileLength = file.length();

                    if(fileLength < lastPosition) {
                        System.out.println("File was rotated. Restarting position to start");
                        lastPosition = 0;
                    }

                    if(fileLength > lastPosition) {
                        try(RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                            raf.seek(lastPosition);

                            String line;
                            while((line = raf.readLine()) != null) {
                                LogEntry entry = reader.parseLine(line);
                                if(entry != null) {
                                    InMemoryLogStore.getInstance().addLog(entry);
                                    System.out.println("New log entry detected:" + entry);
                                }
                            }
                            lastPosition = raf.getFilePointer();
                        }
                    }
                    Thread.sleep(2000);
                } catch (IOException e) {
                    System.err.println("Error reading log file : " + e.getMessage());
                } catch (InterruptedException e) {
                    System.out.println("Monitor thread interrupted.");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("Log monitoring stopped for : " + path);
        });

        monitorThread.setDaemon(true);
        monitorThread.setName("log-monitor-thread");
        monitorThread.start();
    }

    @Override
    public void stopMonitoring() {
        running.set(false);
        if(monitorThread != null && monitorThread.isAlive()) {
            monitorThread.interrupt();
        }
    }
}
