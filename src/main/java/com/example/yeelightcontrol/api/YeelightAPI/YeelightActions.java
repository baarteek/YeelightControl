package com.example.yeelightcontrol.api.YeelightAPI;

import com.example.yeelightcontrol.api.retrievers.ExchangeRateRetriever;
import com.example.yeelightcontrol.api.retrievers.GoldPriceRetriever;

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

    public void changeTransitionEffect(String transitionEffect) {
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


    public void pulseEffect(int pulseSpeed) throws IOException {
        String flowExpression = String.format("%d, 1, 1, 10, %d, 1, 1, 100", pulseSpeed, pulseSpeed);
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
            GoldPriceRetriever goldPriceRetriever = new GoldPriceRetriever();
            double goldPrice = goldPriceRetriever.getGoldPrice();
            int brightness = (int) (goldPrice / 500 * 100);
            brightness = Math.max(1, Math.min(100, brightness));

            bulb.sendCommand(YeelightCommand.generateSetColorTemperatureCommand(3000));
            bulb.sendCommand(YeelightCommand.generateSetRGBCommand(255, 150, 0));
            bulb.sendCommand(YeelightCommand.generateSetBrightCommand(brightness));
    }

    public void setColorBasedOnEuroRate() throws IOException {
        ExchangeRateRetriever rateRetriever = new ExchangeRateRetriever();
        double euroRate = rateRetriever.getEuroExchangeRate();
        setColorBasedOnExchangeRate(euroRate);
    }

    public void setColorBasedOnDollarRate() throws IOException {
        ExchangeRateRetriever rateRetriever = new ExchangeRateRetriever();
        double dollarRate = rateRetriever.getDollarExchangeRate();
        setColorBasedOnExchangeRate(dollarRate);
    }

    public void setColorBasedOnBritishPoundRate() throws IOException {
        ExchangeRateRetriever rateRetriever = new ExchangeRateRetriever();
        double britishPoundRate = rateRetriever.getBritishPoundExchangeRate();
        setColorBasedOnExchangeRate(britishPoundRate);
    }

    private void setColorBasedOnExchangeRate(double exchangeRate) throws IOException {
        adjustColorBasedOnExchangeRate(exchangeRate);
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

    public void setColorBasedOnTemperature(double temperature) throws IOException {
        int red, blue;

        if(temperature <= -10) {
            blue = 255;
            red = 0;
        } else if (temperature >= 30) {
            red = 255;
            blue = 0;
        } else {
            double tempScale = (temperature + 10) / 40;
            red = (int)(255 * tempScale);
            blue = 255 - red;
        }

        setColorRGB(red, 0, blue);
    }

    public void changeLightBehaviorBasedOnWeatherCode(int weatherCode) {
        try {
            if (weatherCode == 0) {
                setColorRGB(255, 255, 0); // Clear sky - Yellow
            } else if (weatherCode >= 1 && weatherCode <= 3) {
                setColorRGB(128, 128, 160); // Mainly clear, partly cloudy, and overcast - Gray
            } else if (weatherCode == 45 || weatherCode == 48) {
                setColorRGB(192, 192, 192); // Fog and depositing rime fog - Light Gray
            } else if (weatherCode >= 51 && weatherCode <= 57) {
                setColorRGB(0, 0, 128); // Drizzle and Freezing Drizzle - Blue
            } else if (weatherCode >= 61 && weatherCode <= 67) {
                setColorRGB(0, 0, 255); // Rain and Freezing Rain - Dark Blue
            } else if (weatherCode >= 71 && weatherCode <= 77) {
                setColorRGB(255, 255, 255); // Snow fall and Snow grains - White
            } else if (weatherCode >= 80 && weatherCode <= 86) {
                setColorRGB(128, 0, 0); // Rain showers and Snow showers - Dark Red
            } else if (weatherCode == 95 || weatherCode >= 96) {
                setColorRGB(255, 0, 0); // Thunderstorm and Thunderstorm with hail - Red
            } else {
                setColorRGB(255, 255, 255); // Unknown weather code - White
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeBulbColorBasedOnWindSpeed(double windSpeed) throws IOException {
        double[] beaufortScale = new double[]{0.0, 0.3, 1.6, 3.4, 5.5, 8.0, 10.8, 13.9, 17.2, 20.8, 24.5, 28.5, 32.7, 45};

        int index = (int)(Math.log(windSpeed / beaufortScale[1]) / Math.log(2));

        int red, green, blue;
        if (windSpeed > beaufortScale[13]) {
            red = 255;
            green = 0;
            blue = 0;
        } else {
            red = 0;
            green = (int) (255.0 * index / 10.0);
            blue = (int) (255.0 * (10 - index) / 10.0);
        }

        setColorRGB(red, green, blue);
    }

    public String getInfo(String... properties) throws IOException {
        String command = YeelightCommand.generateGetPropertiesCommand(properties);
        bulb.sendCommand(command);

        return bulb.readResponse();
    }
}
