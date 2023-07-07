package org.example.retrievers;

import com.google.javascript.jscomp.jarjar.com.google.common.reflect.TypeToken;
import com.google.javascript.jscomp.jarjar.com.google.gson.Gson;
import com.google.javascript.jscomp.jarjar.com.google.gson.GsonBuilder;
import com.google.javascript.jscomp.jarjar.com.google.gson.JsonDeserializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.runtime.ObjectMethods;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WeatherInfoRetriever {
    private String API_URL;

    public WeatherInfoRetriever(double latitude, double longitude) {
        if(!areCoordinatesValid(latitude, longitude)) {
            throw new IllegalArgumentException("Invalid coordinates. Latitude should be between -90 and 90, longitude should be between -180 and 180.");
        } else {
            API_URL = createURL(latitude, longitude);
        }
    }

    private String createURL(double latitude, double longitude) {
        return String.format("https://api.open-meteo.com/v1/forecast?latitude=%.6f&longitude=%.6f&current_weather=true&timezone=Europe%%2FBerlin&forecast_days=1", latitude, longitude);
    }

    private boolean areCoordinatesValid(double latitude, double longitude) {
        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            return false;
        }
        return true;
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
        Gson gson = new Gson();
        Map<String, Object> fullData = gson.fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());
        Map<String, Object> weatherData = (Map<String, Object>) fullData.get("current_weather");

        Map<String, String> result = new HashMap<>();
        result.put("temperature", String.valueOf(weatherData.get("temperature")));
        result.put("windspeed", String.valueOf(weatherData.get("windspeed")));
        result.put("winddirection", String.valueOf(weatherData.get("winddirection")));
        result.put("weathercode", String.valueOf(weatherData.get("weathercode")));
        result.put("is_day", String.valueOf(weatherData.get("is_day")));
        result.put("time", (String) weatherData.get("time"));

        return result;
    }
}
