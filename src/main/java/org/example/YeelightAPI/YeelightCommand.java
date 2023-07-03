package org.example.YeelightAPI;


public class YeelightCommand {
    private static String transitionEffect = "smooth";
    private static int duration = 500;

    public static void setDuration(int value) {
        if(value > 0) {
            duration = value;
        } else {
            System.out.println("The specified duration value is not valid.");
        }
    }

    public static void setTransitionEffect(String value) {
        if(transitionEffect.equals("smooth") || transitionEffect.equals("sudden")) {
            transitionEffect = value;
        } else {
            System.out.println("The specified transition effect name is not valid.");
        }
    }

    public static String generateTurnOnCommand() {
        return String.format("{\"id\":1,\"method\":\"set_power\",\"params\":[\"on\", \"%s\", %d]}\r\n", transitionEffect, duration);
    }

    public static String generateTurnOffCommand()  {
        return String.format("{\"id\":2,\"method\":\"set_power\",\"params\":[\"off\", \"%s\", %d]}\r\n", transitionEffect, duration);
    }

    public static String generateSetColorCommand(int red, int green, int blue) {
        int rgb = red * 65536 + green * 256 + blue;
        return String.format("{\"id\":3, \"method\":\"set_rgb\", \"params\":[%d, \"%s\", %d]}\r\n", rgb, transitionEffect, duration);
    }

    public static String generateSetBrightCommand(int bright) {
        return String.format("{\"id\":4, \"method\":\"set_bright\", \"params\":[%d, \"%s\", %d]}\r\n", bright, transitionEffect, duration);
    }

    public static String generateSetColorTemperatureCommand(int colorTemperature) {
        return String.format("{\"id\":5, \"method\":\"set_ct_abx\", \"params\":[%d, \"%s\", %d]}\r\n", colorTemperature, transitionEffect, duration);
    }

    public static String generateSetNameCommand(String name) {
        return String.format("{\"id\":6, \"method\":\"set_name\", \"params\":[\"%s\"]}\r\n", name);
    }

    public static String generateSetAdjustCommand(String adjustType, String prop) {
        return String.format("{\"id\":7, \"method\":\"set_adjust\", \"params\":[\"%s\", \"%s\"]}\r\n", adjustType, prop);
    }

}
