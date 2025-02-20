package com.example.weatherapi.client;

import com.example.weatherapi.config.CacheConfig;
import com.example.weatherapi.exception.WeatherApiException;
import com.example.weatherapi.model.WeatherResponse;
import com.example.weatherapi.model.WeatherResponse.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.squareup.moshi.Moshi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class WeatherClient {
    private static final Logger logger = LoggerFactory.getLogger(WeatherClient.class);

    private final String apiKey;
    private final String baseUrl;
    private final Moshi moshi;
    private final WeatherResponseAdapter adapter;
    private final CacheConfig cacheConfig;

    public WeatherClient(@Value("${weather.api.key}") String apiKey,
                         @Value("${weather.api.base-url}") String baseUrl,
                         CacheConfig cacheConfig) {
        this.moshi = new Moshi.Builder().add(new WeatherResponseAdapter()).build();
        this.adapter = new WeatherResponseAdapter();
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.cacheConfig = cacheConfig;
    }

    @Cacheable(value = "weather", key = "#city", unless = "#result == null")
    public WeatherResponse fetchWeather(String city) {
        try {
            logger.info("Fetching weather data for city: {}", city);

            HttpResponse<JsonNode> response = Unirest.get(baseUrl)
                    .queryString("q", city)
                    .queryString("appid", apiKey)
                    .asJson();

            if (response.getStatus() != 200) {
                throw new WeatherApiException("API request failed: " + response.getBody());
            }

            String jsonResponse = response.getBody().toString();
            WeatherResponseJson parsedJson = moshi.adapter(WeatherResponseJson.class).fromJson(jsonResponse);

            if (parsedJson == null) {
                throw new WeatherApiException("Failed to parse API response");
            }
            cacheConfig.manageCacheLimit(city);
            return adapter.fromJson(parsedJson);
        } catch (Exception e) {
            logger.error("Error fetching weather data", e);
            throw new WeatherApiException("Failed to fetch weather data", e);
        }
    }
}