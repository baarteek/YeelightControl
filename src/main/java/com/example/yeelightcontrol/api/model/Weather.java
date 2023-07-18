package com.example.yeelightcontrol.api.model;

import com.example.yeelightcontrol.api.retrievers.WeatherInfoRetriever;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

public class Weather {
    private WeatherInfoRetriever retriever;
    private double code;
    private double temperature;
    private double windSpeed;
    private double windDirection;
    private Date date;

    public Weather(double latitude, double longitude) throws IOException {
        retriever = new WeatherInfoRetriever(latitude, longitude);
        Map<String, String> weatherInfo = retriever.getWeatherInfoMap();
        code = Double.parseDouble(weatherInfo.get("weathercode"));
        temperature = Double.parseDouble(weatherInfo.get("temperature"));
        windSpeed = Double.parseDouble(weatherInfo.get("windspeed"));
        windDirection = Double.parseDouble(weatherInfo.get("winddirection"));
        date = parseData(weatherInfo.get("time"));
    }

    private Date parseData(String time) {
        ZonedDateTime zonedDateTime = LocalDateTime.parse(time + ":00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")).atZone(ZoneId.of("Europe/Berlin"));
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }

    public String getWeatherDescription() {
        if (code == 0) {
            return "Clear sky";
        } else if (code >= 1 && code <= 3) {
            return "Mainly clear, partly cloudy, and overcast";
        } else if (code == 45 || code == 48) {
            return "Fog and depositing rime fog";
        } else if (code >= 51 && code <= 57) {
            return "Drizzle and Freezing Drizzle";
        } else if (code >= 61 && code <= 67) {
            return "Rain and Freezing Rain";
        } else if (code >= 71 && code <= 77) {
            return "Snow fall and Snow grains";
        } else if (code >= 80 && code <= 86) {
            return "Rain showers and Snow showers";
        } else if (code == 95 || code >= 96) {
            return "Thunderstorm and Thunderstorm with hail";
        } else {
            return "Unknown weather code";
        }
    }

    public Date getDate() {
        return date;
    }

    public double getCode() {
        return code;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public double getWindSpeed() {
        return windSpeed;
    }
}
