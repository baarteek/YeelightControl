package org.example.retrievers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExchangeRateRetriever {
    private final String EUR_API_URL = "http://api.nbp.pl/api/exchangerates/rates/A/eur/";
    private final String USD_API_URL = "http://api.nbp.pl/api/exchangerates/rates/A/usd/";
    private final String GBP_API_URL = "https://api.nbp.pl/api/exchangerates/rates/A/gbp/";

    public Double getEuroExchangeRate() {
        return retrieveExchangeRateFromAPI(EUR_API_URL);
    }

    public Double getDollarExchangeRate() {
        return retrieveExchangeRateFromAPI(USD_API_URL);
    }

    public Double getBritishPoundExchangeRate() {
        return retrieveExchangeRateFromAPI(GBP_API_URL);
    }

    private Double retrieveExchangeRateFromAPI(String api_url) {
        try {
            String rateInfoJson = getExchangeRateInfo(api_url);
            return extractExchangeRateFromJson(rateInfoJson);
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String getExchangeRateInfo(String url_api) throws IOException {
        URL url = new URL(url_api);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        if(connection.getResponseCode() != 200) {
            throw new RuntimeException("Field: HTTP error code: " + connection.getResponseCode());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String output = reader.readLine();
        connection.disconnect();

        return output;
    }

    private double extractExchangeRateFromJson(String rateJson) throws IllegalAccessException {
        JSONObject jsonObject = new JSONObject(rateJson);
        JSONArray ratesArray = jsonObject.getJSONArray("rates");
        if(ratesArray.isEmpty()) {
            throw new IllegalAccessException("No rates avalilable in the provided JSON.");
        }
        JSONObject rateObject = ratesArray.getJSONObject(0);
        return rateObject.getDouble("mid");
    }
}
