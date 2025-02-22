package com.example.weatherapi.model;

import com.squareup.moshi.Json;

import java.util.List;

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


    public record Weather(
            @Json(name = "main") String main,
            @Json(name = "description") String description
    ) {
    }

    public record Temperature(
            @Json(name = "temp") double temp,
            @Json(name = "feels_like") Double feelsLike
    ) {
    }

    public record Wind(
            @Json(name = "speed") double speed
    ) {
    }

    public record Sys(
            @Json(name = "sunrise") long sunrise,
            @Json(name = "sunset") long sunset
    ) {
    }

    public record WeatherResponseJson(
            @Json(name = "weather") List<Weather> weather,
            @Json(name = "main") Temperature temperature,
            @Json(name = "visibility") int visibility,
            @Json(name = "wind") Wind wind,
            @Json(name = "dt") long dt,
            @Json(name = "sys") Sys sys,
            @Json(name = "timezone") Integer timezone,
            @Json(name = "name") String name
    ) {
    }
}



