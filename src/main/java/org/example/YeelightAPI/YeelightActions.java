package org.example.YeelightAPI;

import org.example.Main;
import org.example.retrievers.GoldPriceRetriever;

import java.io.IOException;

public class YeelightActions {
    public void adjustBrightnessBasedOnGoldPrice() throws IOException {
        try {
            GoldPriceRetriever  goldPriceRetriever = new GoldPriceRetriever();
            double goldPrice = goldPriceRetriever.getGoldPrice();
            int brightness = (int) (goldPrice / 500 * 100);
            brightness = Math.max(1, Math.min(100, brightness));

            YeelightBulb bulb = new YeelightBulb();
            bulb.connect("192.168.1.4");
            bulb.sendCommand(YeelightCommand.generateSetColorTemperatureCommand(6700));
            bulb.sendCommand(YeelightCommand.generateSetColorCommand(255, 150, 0));
            bulb.sendCommand(YeelightCommand.generateSetBrightCommand(brightness));

            bulb.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
