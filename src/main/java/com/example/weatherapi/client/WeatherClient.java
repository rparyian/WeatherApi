package com.example.weatherapi.client;

import com.example.weatherapi.config.CacheConfig;
import com.example.weatherapi.exception.WeatherApiException;
import com.example.weatherapi.model.WeatherResponse;
import com.example.weatherapi.model.WeatherResponseAdapter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.squareup.moshi.Moshi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
                         WeatherResponseAdapter adapter,
                         CacheConfig cacheConfig) {
        this.moshi = new Moshi.Builder().add(adapter).build();
        this.adapter = adapter;
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

            int status = response.getStatus();
            if (response.getStatus() != 200) {
                logger.error("Weather API returned an error: {} - {}", status, response.getBody());
                throw new WeatherApiException(status, response.getBody().toString());
            }

            try {
                WeatherResponse.WeatherResponseJson parsedJson = moshi.adapter(WeatherResponse.WeatherResponseJson.class).fromJson(response.getBody().toString());
                cacheConfig.manageCacheLimit(city);
                return adapter.fromJson(parsedJson);
            } catch (IOException e) {
                logger.error("Failed to parse OpenWeather API response", e);
                throw new WeatherApiException(502, "{\"message\": \"Invalid response format from OpenWeather API\"}");
            }


        } catch (UnirestException e) {
            Throwable cause = e.getCause();
            if (cause instanceof java.net.UnknownHostException ||
                    cause instanceof java.net.SocketTimeoutException) {
                logger.error("Network error when calling OpenWeather API", e);
                throw new WeatherApiException(503, "{\"message\": \"Service Unavailable - Network issue\"}");
            }

            logger.error("Unexpected error when calling OpenWeather API", e);
            throw new WeatherApiException(500, "{\"message\": \"Internal Server Error\"}");
        }
    }
}