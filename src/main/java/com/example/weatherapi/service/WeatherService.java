package com.example.weatherapi.service;

import com.example.weatherapi.client.WeatherClient;
import com.example.weatherapi.model.WeatherResponse;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    private final WeatherClient weatherClient;

    public WeatherService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public WeatherResponse getWeather(String city) {
        return weatherClient.fetchWeather(city);
    }
}
