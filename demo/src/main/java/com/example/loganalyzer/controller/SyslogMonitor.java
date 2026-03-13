package com.example.loganalyzer.controller;

import com.example.loganalyzer.syslog.SyslogListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/syslog")
public class SyslogMonitor {

    private final SyslogListener syslogListener;

    public SyslogMonitor(SyslogListener syslogListener) {
        this.syslogListener = syslogListener;
    }

    @PostMapping("/start")
    public String startListener() {
        syslogListener.start();
        return "Syslog listener started on UDP port 514.";
    }

    @PostMapping("/stop")
    public String stopListener() {
        syslogListener.stop();
        return "Syslog listener stopped.";
    }
}
