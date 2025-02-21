package com.example.weatherapi.service;

import com.example.weatherapi.config.PollingConfig;
import jakarta.annotation.PostConstruct;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PollingAspect {
    private static final Logger logger = LoggerFactory.getLogger(PollingAspect.class);

    private final PollingConfig pollingConfig;
    private final WeatherPollingService weatherPollingService;

    public PollingAspect(PollingConfig pollingConfig,
                         WeatherPollingService weatherPollingService) {
        this.pollingConfig = pollingConfig;
        this.weatherPollingService = weatherPollingService;
    }

    @PostConstruct
    public void init() {
        if (pollingConfig.isEnabled()) {
            logger.info("Polling is enabled, starting polling service.");
            weatherPollingService.startPolling();
        } else {
            logger.info("Polling is disabled, skipping polling.");
        }
    }
}
