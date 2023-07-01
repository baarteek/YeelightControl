package org.example.YeelightAPI;

import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class YeelightConnector {
    private Socket yeelightSocket;
    private final int port = 55443;
    private PrintWriter out;
    private YeelightCommand commandGenerator;

    public YeelightConnector(String ip) throws IOException {
        yeelightSocket = new Socket(ip, port);
        out = new PrintWriter(yeelightSocket.getOutputStream(), true);
        commandGenerator = new YeelightCommand();
    }

    public void sendCommand(String command) {
        out.print(command);
        out.flush();
    }

    public void close() throws IOException {
        yeelightSocket.close();
    }
}
