package com.example.weatherapi.model;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import org.springframework.stereotype.Component;

@Component
public class WeatherResponseAdapter {

    @FromJson
    public WeatherResponse fromJson(WeatherResponse.WeatherResponseJson json) {
        int timezone = (json.timezone() != null) ? json.timezone() : 0;

        double feelsLike = (json.temperature().feelsLike() != null)
                ? json.temperature().feelsLike()
                : json.temperature().temp();

        WeatherResponse.Temperature adaptedTemp = new WeatherResponse.Temperature(
                json.temperature().temp(), feelsLike);

        return new WeatherResponse(
                json.weather(),
                adaptedTemp,
                json.visibility(),
                json.wind(),
                json.dt(),
                json.sys(),
                json.timezone(),
                json.name()
        );
    }

    @ToJson
    public WeatherResponse.WeatherResponseJson toJson(WeatherResponse response) {
        return new WeatherResponse.WeatherResponseJson(
                response.weather(),
                response.temperature(),
                response.visibility(),
                response.wind(),
                response.datetime(),
                response.sys(),
                response.timezone(),
                response.name()
        );
    }
}
