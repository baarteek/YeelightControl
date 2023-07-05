package org.example.YeelightAPI;

import org.example.Main;
import org.example.retrievers.GoldPriceRetriever;

import java.io.IOException;

public class YeelightActions {
    private YeelightBulb bulb;

    public YeelightActions(YeelightBulb bulb) {
        this.bulb = bulb;
    }

    public void adjustBrightnessBasedOnGoldPrice() throws IOException {
        try {
            GoldPriceRetriever  goldPriceRetriever = new GoldPriceRetriever();
            double goldPrice = goldPriceRetriever.getGoldPrice();
            int brightness = (int) (goldPrice / 500 * 100);
            brightness = Math.max(1, Math.min(100, brightness));

            bulb.sendCommand(YeelightCommand.generateSetColorTemperatureCommand(3000));
            bulb.sendCommand(YeelightCommand.generateSetColorCommand(255, 150, 0));
            bulb.sendCommand(YeelightCommand.generateSetBrightCommand(brightness));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
