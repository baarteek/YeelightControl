package com.example.yeelightcontrol.api;

import com.example.yeelightcontrol.api.YeelightAPI.YeelightActions;
import com.example.yeelightcontrol.api.YeelightAPI.YeelightBulb;
import com.example.yeelightcontrol.api.model.Weather;
import com.example.yeelightcontrol.api.retrievers.GoldPriceRetriever;
import com.example.yeelightcontrol.api.retrievers.WeatherInfoRetriever;
import com.example.yeelightcontrol.ui.utils.DialogHelper;

import java.io.IOException;
import java.net.ConnectException;

public class Main {
    public static void main(String[] args) {
        YeelightBulb bulb = new YeelightBulb("Bulb", "192.168.1.4");
        YeelightActions actions = new YeelightActions(bulb);

        try {
            bulb.connect();

            System.out.println(actions.getInfo("power", "bright", "color_mode", "rgb", "ct", "hue", "sat"));


            bulb.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}