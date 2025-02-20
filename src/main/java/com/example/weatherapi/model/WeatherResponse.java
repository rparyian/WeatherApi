package com.example.weatherapi.model;

import com.squareup.moshi.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@JsonClass(generateAdapter = true)
public record WeatherResponse(
        @Json(name = "weather") List<Weather> weather,
        @Json(name = "temperature") Temperature temperature,
        @Json(name = "visibility") int visibility,
        @Json(name = "wind") Wind wind,
        @Json(name = "datetime") long datetime,
        @Json(name = "sys") Sys sys,
        @Json(name = "timezone") Integer timezone,
        @Json(name = "name") String name
) {

    public static class WeatherResponseAdapter {
        @FromJson
        public WeatherResponse fromJson(WeatherResponseJson json) {
            int timezone = (json.timezone() != null) ? json.timezone() : 0;

            double feelsLike = (json.temperature().feelsLike() != null)
                    ? json.temperature().feelsLike()
                    : json.temperature().temp();

            Temperature adaptedTemp = new Temperature(json.temperature().temp(), feelsLike);

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
        WeatherResponseJson toJson(WeatherResponse response) {
            return new WeatherResponseJson(
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

    public record Weather(
            @Json(name = "main") String main,
            @Json(name = "description") String description
    ) {}

    public record Temperature(
            @Json(name = "temp") double temp,
            @Json(name = "feels_like") Double feelsLike
    ) {}

    public record Wind(
            @Json(name = "speed") double speed
    ) {}

    public record Sys(
            @Json(name = "sunrise") long sunrise,
            @Json(name = "sunset") long sunset
    ) {}

    public record WeatherResponseJson(
            @Json(name = "weather") List<Weather> weather,
            @Json(name = "main") Temperature temperature,
            @Json(name = "visibility") int visibility,
            @Json(name = "wind") Wind wind,
            @Json(name = "dt") long dt,
            @Json(name = "sys") Sys sys,
            @Json(name = "timezone") Integer timezone,
            @Json(name = "name") String name
    ) {}
}



