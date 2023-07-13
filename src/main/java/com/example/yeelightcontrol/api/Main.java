package com.example.yeelightcontrol.api;

import com.example.yeelightcontrol.api.YeelightAPI.YeelightActions;
import com.example.yeelightcontrol.api.YeelightAPI.YeelightBulb;
import com.example.yeelightcontrol.api.retrievers.GoldPriceRetriever;

import java.io.IOException;
import java.net.ConnectException;

public class Main {
    public static void main(String[] args) {
        YeelightBulb bulb = new YeelightBulb("Bulb", "192.168.1.4");
        YeelightActions bulbActions = new YeelightActions(bulb);

        try {
            bulb.connect();

            bulbActions.setColorRGB(0,255,0);

            String response = bulb.readResponse();
            System.out.println(response);

            bulb.disconnect();
        }catch (ConnectException ce) {
            System.out.println("Connection timed out, please make sure the IP is correct and the device is connected to the network");
            ce.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}