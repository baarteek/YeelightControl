package org.example.YeelightAPI;

import java.io.*;
import java.net.*;

public class YeelightBulb {
    private static final int PORT = 55443;
    private final int TIMEOUT = 5000;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;


    public void connect(String ip) throws IOException {
        socket = new Socket(ip, PORT);
        socket.setSoTimeout(TIMEOUT);
        reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
    }

    public void disconnect() throws IOException {
        socket.close();
    }

    public void sendCommand(String command) throws IOException {
        writer.write(command);
        writer.flush();
    }

    public String readResponse() throws IOException {
        return reader.readLine();
    }
}