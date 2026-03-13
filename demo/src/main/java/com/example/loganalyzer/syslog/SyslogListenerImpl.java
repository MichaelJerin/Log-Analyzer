package com.example.loganalyzer.syslog;

import com.example.loganalyzer.model.LogEntry;
import com.example.loganalyzer.model.LogSource;
import com.example.loganalyzer.normalizer.LogNormalizer;
import com.example.loganalyzer.normalizer.LogNormalizerImpl;
import com.example.loganalyzer.util.InMemoryLogStore;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class SyslogListenerImpl implements SyslogListener {

    private static final int SYSLOG_PORT = 514;
    private static final int BUFFER_SIZE = 1024;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final SyslogParser parser = new SyslogParser();

    private final LogNormalizer normalizer = new LogNormalizerImpl();

    private Thread listenerThread;
    private DatagramSocket socket;

    @Override
    public void start() {

        if(running.get()) {
            System.out.println("SysLog Listener is already running.");
            return;
        }

        running.set(true);

        listenerThread = new Thread(() -> {

            try {
                socket = new DatagramSocket(SYSLOG_PORT);
                System.out.println("Syslog listener started on UDP port " + SYSLOG_PORT);

                byte[] buffer = new byte[BUFFER_SIZE];

                while(running.get()) {

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String rawMessage = new String(packet.getData(), 0, packet.getLength()).trim();
                    String senderIP = packet.getAddress().getHostAddress();

                    LogEntry entry = parser.parse(rawMessage, senderIP);

                    if(entry != null) {
                        entry.setSource(LogSource.SYSLOG);
                        LogEntry normalizedEntry = normalizer.normalize(entry);
                        InMemoryLogStore.getInstance().addLog(normalizedEntry);
                        System.out.println("Stored Syslog Entry : " + normalizedEntry);
                    }
                }
            } catch (Exception e) {
                if(running.get()) {
                    System.out.println("Syslog listener error " + e.getMessage());
                }
            } finally {
                if(socket != null && !socket.isClosed()) {
                    socket.close();
                }
                System.out.println("Syslog Listener Stopped");
            }
        });

        listenerThread.setDaemon(true);
        listenerThread.setName("syslog-listener-thread");
        listenerThread.start();
    }

    @Override
    public void stop() {
        running.set(false);
        if(socket != null && !socket.isClosed()) {
            socket.close();
        }

        if(listenerThread != null) {
            listenerThread.interrupt();
        }
        System.out.println("Syslog listener stop requested.");
    }
}
