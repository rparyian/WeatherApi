package com.example.weatherapi.service;

import com.example.weatherapi.client.WeatherClient;
import com.example.weatherapi.config.CacheConfig;
import com.example.weatherapi.model.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class WeatherPollingService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherPollingService.class);

    private final WeatherClient weatherClient;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String CACHE_LIST_KEY = "weather_city_list";

    public WeatherPollingService(WeatherClient weatherClient, RedisTemplate<String, String> redisTemplate) {
        this.weatherClient = weatherClient;
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(fixedRate = 300000)
    public void startPolling() {
        logger.info("Polling job started: Checking for cached cities to update.");

        List<String> cachedCities = redisTemplate.opsForList().range(CACHE_LIST_KEY, 0, -1);
        if (cachedCities == null || cachedCities.isEmpty()) {
            logger.info("No cities found in cache. Skipping polling.");
            return;
        }

        logger.info("Updating weather data for {} cached cities: {}", cachedCities.size(), cachedCities);

        for (String city : cachedCities) {
            try {
                weatherClient.fetchWeather(city); // Refresh weather data
                logger.info("Successfully updated weather for '{}'.", city);
            } catch (Exception e) {
                logger.error("Failed to update weather for '{}': {}", city, e.getMessage());
            }
        }

        logger.info("Polling job finished.");
    }
}
