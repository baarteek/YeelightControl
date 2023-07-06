package org.example.YeelightAPI;

import org.example.Main;
import org.example.retrievers.ExchangeRateRetriever;
import org.example.retrievers.GoldPriceRetriever;

import java.io.IOException;

public class YeelightActions {
    private YeelightBulb bulb;

    public YeelightActions(YeelightBulb bulb) {
        this.bulb = bulb;
    }

    public void turnOn() throws IOException {
        bulb.sendCommand(YeelightCommand.generateTurnOnCommand());
    }

    public void turnOff() throws IOException {
        bulb.sendCommand(YeelightCommand.generateTurnOffCommand());
    }

    public void changeTransitionEffect() {
        String transitionEffect = YeelightCommand.getTransitionEffect();
        if(transitionEffect.equals("smooth")) {
            YeelightCommand.setTransitionEffect("sudden");
        } else {
            YeelightCommand.setTransitionEffect("smooth");
        }
    }

    public void setDuration(int duration) {
        YeelightCommand.setDuration(duration);
    }

    public void setColorRGB(int red, int green, int blue) throws IOException {
        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));

        bulb.sendCommand(YeelightCommand.generateSetRGBCommand(red, green, blue));
    }

    public void setColorHSV(int hue, int saturation) throws IOException {
        hue = Math.max(0, Math.min(359, hue));
        saturation = Math.max(0, Math.min(100, saturation));
        bulb.sendCommand(YeelightCommand.generateSetHSVCommand(hue, saturation));
    }

    public void setBrightness(int brightness) throws IOException {
        brightness = Math.max(0, Math.min(100, brightness));
        bulb.sendCommand(YeelightCommand.generateSetBrightCommand(brightness));
    }

    public void setColorTemperature(int colorTemperature) throws IOException {
        colorTemperature = Math.max(1700, Math.min(6500, colorTemperature));
        bulb.sendCommand(YeelightCommand.generateSetColorTemperatureCommand(colorTemperature));
    }

    public void adjustProperties(String adjustType, String properties) throws IOException {
        bulb.sendCommand(YeelightCommand.generateAdjustCommand(adjustType, properties));
    }

    public void setName(String name) throws IOException {
        bulb.sendCommand(YeelightCommand.generateSetNameCommand(name));
    }

    public void rainbowColorEffect(int transitionDuration) throws IOException {
        int saturation = 100;
        for(int hue = 0; hue < 360; hue+=5) {
            bulb.sendCommand(YeelightCommand.generateSetHSVCommand(hue, saturation));
            sleep(transitionDuration);
        }
    }


    public void pulseEffect(int pulseSpeed, int startBrightness, int endBrightness, int startColor, int endColor) throws IOException {
        String flowExpression = String.format("%d, 1, %d, %d, %d, 1, %d, %d", pulseSpeed, startColor, startBrightness, pulseSpeed, endColor, endBrightness);
        bulb.sendCommand(YeelightCommand.generateStartColorFlowCommand(0, 0, flowExpression));
    }

    private void sleep(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void adjustBrightnessAndColorBasedOnGoldPrice() throws IOException {
        try {
            GoldPriceRetriever  goldPriceRetriever = new GoldPriceRetriever();
            double goldPrice = goldPriceRetriever.getGoldPrice();
            int brightness = (int) (goldPrice / 500 * 100);
            brightness = Math.max(1, Math.min(100, brightness));

            bulb.sendCommand(YeelightCommand.generateSetColorTemperatureCommand(3000));
            bulb.sendCommand(YeelightCommand.generateSetRGBCommand(255, 150, 0));
            bulb.sendCommand(YeelightCommand.generateSetBrightCommand(brightness));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setColorBasedOnEuroRate() {
        ExchangeRateRetriever rateRetriever = new ExchangeRateRetriever();
        double euroRate = rateRetriever.getEuroExchangeRate();
        setColorBasedOnExchangeRate(euroRate);
    }

    public void setColorBasedOnDollarRate() {
        ExchangeRateRetriever rateRetriever = new ExchangeRateRetriever();
        double dollarRate = rateRetriever.getDollarExchangeRate();
        setColorBasedOnExchangeRate(dollarRate);
    }

    public void setColorBasedOnBritishPoundRate() {
        ExchangeRateRetriever rateRetriever = new ExchangeRateRetriever();
        double britishPoundRate = rateRetriever.getBritishPoundExchangeRate();
        setColorBasedOnExchangeRate(britishPoundRate);
    }

    private void setColorBasedOnExchangeRate(double exchangeRate) {
        try {
            adjustColorBasedOnExchangeRate(exchangeRate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adjustColorBasedOnExchangeRate(double exchangeRate) throws IOException {
        double minExchangeRate = 3;
        double maxExchangeRate = 6;
        double normalizedRate = (exchangeRate - minExchangeRate) / (maxExchangeRate - minExchangeRate);
        int redValue = (int) ((1 - normalizedRate) * 255);
        int greenValue = (int) (normalizedRate * 255);
        int blueValue = 255 - Math.abs(redValue - greenValue);
        redValue = Math.max(0, Math.min(255, redValue));
        greenValue = Math.max(0, Math.min(255, greenValue));
        blueValue = Math.max(0, Math.min(255, blueValue));
        bulb.sendCommand(YeelightCommand.generateSetRGBCommand(redValue, greenValue, blueValue));
    }

    public void morseCodeSignal(String morseCode, int dotDuration) throws IOException {
        for (char c : morseCode.toCharArray()) {
            switch (c) {
                case '.':
                    bulb.sendCommand(YeelightCommand.generateSetBrightCommand(90));
                    sleep(dotDuration);
                    bulb.sendCommand(YeelightCommand.generateSetBrightCommand(10));
                    sleep(dotDuration);
                    break;
                case '-':
                    bulb.sendCommand(YeelightCommand.generateSetBrightCommand(90));
                    sleep(3 * dotDuration);
                    bulb.sendCommand(YeelightCommand.generateSetBrightCommand(10));
                    sleep(dotDuration);
                    break;
            }
        }
    }
}
