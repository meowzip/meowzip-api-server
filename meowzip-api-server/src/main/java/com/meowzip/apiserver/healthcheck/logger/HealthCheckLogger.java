package com.meowzip.apiserver.healthcheck.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class HealthCheckLogger {

    private LocalDateTime loggedDateTime;

    public void log() {
        if (!isPast()) {
            return;
        }

        log.info("I'm healthy");
        loggedDateTime = LocalDateTime.now();
    }

    private boolean isPast() {
        if (loggedDateTime == null) {
            return true;
        }

        LocalDateTime minusMinutes = LocalDateTime.now().minusMinutes(5);

        return loggedDateTime.isBefore(minusMinutes);
    }
}
