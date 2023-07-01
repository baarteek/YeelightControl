package org.example.YeelightAPI;

public class YeelightCommand {
    public String generateTurnOnCommand() {
        return "{\"id\":1,\"method\":\"set_power\",\"params\":[\"on\", \"smooth\", 500]}\\r\\n";
    }

    public String generateTurnOffComand()  {
        return "{\"id\":1,\"method\":\"set_power\",\"params\":[\"off\", \"smooth\", 500]}\\r\\n";
    }
}
