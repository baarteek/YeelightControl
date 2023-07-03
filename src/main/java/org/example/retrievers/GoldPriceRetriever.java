package org.example.retrievers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoldPriceRetriever {
    private static final String API_URL = "http://api.nbp.pl/api/cenyzlota";

    public Double getGoldPrice() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        if(connection.getResponseCode() != 200) {
            throw new RuntimeException("Failed: HTTP error code: " + connection.getResponseCode());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String output = reader.readLine();
        connection.disconnect();

        JSONArray jsonArray = new JSONArray(output);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        return jsonObject.getDouble("cena");
    }
}
