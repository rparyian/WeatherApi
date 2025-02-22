
Weather API Client

Project Overview

This project is a RESTful service written in Java using Spring Boot, designed to retrieve weather data from the OpenWeather API. The service includes request caching with Redis, error handling, and unit tests for key components.

The project was developed as a test assignment to demonstrate skills in Java, Spring Boot, REST API integration, caching, and testing.

Key Features
Retrieve weather data by city name via the OpenWeather API.
Cache query results in Redis with a limit of the 10 most recent cities.
Handle API errors (e.g., 404 for unknown cities, 503 for network issues).
REST endpoint for fetching weather: GET /weather?city={city}.
Technologies
Java: 17 
Spring Boot: 3.4.2 
Unirest: For making HTTP requests to the OpenWeather API
Moshi: For parsing JSON responses
Redis: For caching data
SLF4J: For logging
JUnit 5 & Mockito: For unit testing

Requirements
Java 17+
Maven 3.8+
Redis (locally or via Docker)
OpenWeatherMap API key (obtainable from openweathermap.org)

Steps to Run
-Clone the Repository
-Add your OpenWeatherMap API key into Docker-compose.yml
-mvn clean install
-docker-compose up --build
