package org.example;

import org.example.YeelightAPI.YeelightActions;
import org.example.YeelightAPI.YeelightBulb;
import org.example.YeelightAPI.YeelightCommand;
import org.example.retrievers.GoldPriceRetriever;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        YeelightBulb bulb = new YeelightBulb();

        try {
            YeelightActions bulbActions = new YeelightActions();
            bulbActions.adjustBrightnessBasedOnGoldPrice();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}