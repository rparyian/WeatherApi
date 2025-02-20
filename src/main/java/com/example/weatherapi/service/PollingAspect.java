package com.example.weatherapi.service;

import com.example.weatherapi.config.PollingConfig;
import jakarta.annotation.PostConstruct;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PollingAspect {
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
            weatherPollingService.startPolling();
        }
    }
}
