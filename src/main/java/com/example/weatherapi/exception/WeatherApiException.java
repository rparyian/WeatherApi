package com.example.weatherapi.exception;

public class WeatherApiException extends RuntimeException {
    private final int statusCode;
    private final String responseBody;

    public WeatherApiException(int statusCode, String responseBody) {
        super("Weather API error: " + statusCode);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
