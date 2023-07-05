package org.example;

import org.example.YeelightAPI.YeelightActions;
import org.example.YeelightAPI.YeelightBulb;
import org.example.YeelightAPI.YeelightCommand;
import org.example.retrievers.ExchangeRateRetriever;
import org.example.retrievers.GoldPriceRetriever;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        YeelightBulb bulb = new YeelightBulb();
        YeelightActions bulbActions = new YeelightActions(bulb);

        try {
            bulb.connect();

            bulb.sendCommand(YeelightCommand.generateTurnOnCommand());
            bulb.sendCommand(YeelightCommand.generateSetColorTemperatureCommand(6000));
            bulb.sendCommand(YeelightCommand.generateSetColorCommand(255, 0, 0));


            String response = bulb.readResponse();
            System.out.println(response);

            bulb.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}