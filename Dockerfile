# Use a lightweight OpenJDK image
FROM openjdk:23-jdk

# Set the working directory
WORKDIR /app

# Copy the built JAR file
COPY target/WeatherApi-0.0.1-SNAPSHOT.jar WeatherApi.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "WeatherApi.jar"]