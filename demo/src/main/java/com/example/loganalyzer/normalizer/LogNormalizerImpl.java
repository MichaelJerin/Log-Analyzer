package com.example.loganalyzer.normalizer;

import com.example.loganalyzer.model.LogEntry;
import org.springframework.stereotype.Service;

@Service
public class LogNormalizerImpl implements LogNormalizer {

    private final FileLogNormalizer fileLogNormalizer = new FileLogNormalizer();
    private final SyslogNormalizer syslogNormalizer = new SyslogNormalizer();

    @Override
    public LogEntry normalize(LogEntry entry) {

        if(entry == null) return null;

        switch (entry.getSource()) {
            case SYSLOG :
                return syslogNormalizer.normalize(entry);
            case FILE :
            case API :
            default :
                    return fileLogNormalizer.normalize(entry);
        }
    }
}
