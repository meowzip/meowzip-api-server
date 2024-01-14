package com.meowzip.apiserver.healthcheck.controller;

import com.meowzip.apiserver.healthcheck.logger.HealthCheckLogger;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.SQLException;

@Hidden
@Slf4j
@RequiredArgsConstructor
@RestController
public class HealthCheckController {

    private final DataSource dataSource;
    private final HealthCheckLogger healthCheckLogger;

    @GetMapping("/health-check")
    public void healthCheck() {
        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        healthCheckLogger.log();
    }
}
