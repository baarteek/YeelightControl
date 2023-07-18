package com.example.yeelightcontrol.api;

import com.example.yeelightcontrol.api.YeelightAPI.YeelightActions;
import com.example.yeelightcontrol.api.YeelightAPI.YeelightBulb;
import com.example.yeelightcontrol.api.model.Weather;
import com.example.yeelightcontrol.api.retrievers.GoldPriceRetriever;
import com.example.yeelightcontrol.api.retrievers.WeatherInfoRetriever;

import java.io.IOException;
import java.net.ConnectException;

public class Main {
    public static void main(String[] args) {
        try {
            Weather weather = new Weather(50.12, 23.11);

            System.out.println(weather.getTemperature());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}