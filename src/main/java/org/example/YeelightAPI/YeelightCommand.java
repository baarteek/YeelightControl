package org.example.YeelightAPI;

public class YeelightCommand {
    public static String generateTurnOnCommand() {
        return "{\"id\":1,\"method\":\"set_power\",\"params\":[\"on\", \"smooth\", 500]}\r\n";
    }

    public static String generateTurnOffCommand()  {
        return "{\"id\":2,\"method\":\"set_power\",\"params\":[\"off\", \"smooth\", 500]}\r\n";
    }

    public static String generateSetColorCommand(int red, int green, int blue) {
        int rgb = red * 65536 + green * 256 + blue;
        return String.format("{\"id\":3, \"method\":\"set_rgb\", \"params\":[%d, \"smooth\", 500]}\r\n", rgb);
    }
}
