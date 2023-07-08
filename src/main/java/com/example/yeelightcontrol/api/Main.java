package com.example.yeelightcontrol.api;

import com.example.yeelightcontrol.api.YeelightAPI.YeelightActions;
import com.example.yeelightcontrol.api.YeelightAPI.YeelightBulb;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        YeelightBulb bulb = new YeelightBulb("192.168.1.4");
        YeelightActions bulbActions = new YeelightActions(bulb);

        try {
            bulb.connect();

            bulbActions.setColorRGB(100, 200, 255);


            String response = bulb.readResponse();
            System.out.println(response);

            System.out.println(bulbActions.getInfo("rgb", "power"));

            bulb.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}