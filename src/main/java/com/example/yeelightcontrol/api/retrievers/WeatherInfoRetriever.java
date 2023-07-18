package com.example.yeelightcontrol.api.retrievers;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class WeatherInfoRetriever {
    private String API_URL;

    public WeatherInfoRetriever(double latitude, double longitude) {
            API_URL = createURL(latitude, longitude);
    }

    public String createURL(double latitude, double longitude) {
        DecimalFormat decimalFormat = new DecimalFormat("#.######");
        String latitudeStr = decimalFormat.format(latitude).replace(",", ".");
        String longitudeStr = decimalFormat.format(longitude).replace(",", ".");
        return String.format("https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current_weather=true&forecast_days=1", latitudeStr, longitudeStr);
    }

    public Map<String, String> getWeatherInfoMap() throws IOException {
        String weatherInfo = getWeatherInfoFromAPI();
        return extractWeatherInfoFormJson(weatherInfo);
    }

    private String getWeatherInfoFromAPI() throws IOException{
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        if(connection.getResponseCode() != 200) {
            throw new RuntimeException("Field HTTP error code: " + connection.getResponseCode());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String output = reader.readLine();
        connection.disconnect();

        return output;
    }

    private Map<String, String> extractWeatherInfoFormJson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject weatherData = jsonObject.getJSONObject("current_weather");

        Map<String, String> result = new HashMap<>();
        result.put("temperature", String.valueOf(weatherData.getDouble("temperature")));
        result.put("windspeed", String.valueOf(weatherData.getDouble("windspeed")));
        result.put("winddirection", String.valueOf(weatherData.getDouble("winddirection")));
        result.put("weathercode", String.valueOf(weatherData.getInt("weathercode")));
        result.put("is_day", String.valueOf(weatherData.get("is_day")));
        result.put("time", weatherData.getString("time"));

        return result;
    }
}
