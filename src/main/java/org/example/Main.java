package org.example;

import org.example.YeelightAPI.YeelightActions;
import org.example.YeelightAPI.YeelightBulb;
import org.example.YeelightAPI.YeelightCommand;
import org.example.model.Weather;
import org.example.retrievers.ExchangeRateRetriever;
import org.example.retrievers.GoldPriceRetriever;
import org.example.retrievers.WeatherInfoRetriever;
import org.example.retrievers.WeatherInfoRetriever;
import org.example.utils.MorseCodeTranslator;
import org.example.utils.MorseCodeTranslator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        YeelightBulb bulb = new YeelightBulb();
        YeelightActions bulbActions = new YeelightActions(bulb);

        try {
            bulb.connect();

            Weather weather = new Weather(50.7808, 22.8844);

            String morseCode = MorseCodeTranslator.textToMorseCode("sos");
            bulbActions.morseCodeSignal(morseCode, 400);


            String response = bulb.readResponse();
            System.out.println(response);

            bulb.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}