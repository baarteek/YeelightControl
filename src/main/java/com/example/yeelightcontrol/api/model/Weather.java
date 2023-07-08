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

    public Weather(double latitude, double longitude) {
        retriever = new WeatherInfoRetriever(latitude, longitude);
        try {
            Map<String, String> weatherInfo = retriever.getWeatherInfoMap();
            code = Double.parseDouble(weatherInfo.get("weathercode"));
            temperature = Double.parseDouble(weatherInfo.get("temperature"));
            windSpeed = Double.parseDouble(weatherInfo.get("windspeed"));
            windDirection = Double.parseDouble(weatherInfo.get("winddirection"));
            date = parseData(weatherInfo.get("time"));
            System.out.println(date);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Date parseData(String time) {
        ZonedDateTime zonedDateTime = LocalDateTime.parse(time + ":00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")).atZone(ZoneId.of("Europe/Berlin"));
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
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
