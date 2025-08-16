package com.example.weatherapplication;

import java.net.http.*;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherService {
    private static final String API_KEY = "0c7e3419e6944a0f94652715251608";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static JSONObject getCurrentWeather(String city) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String url = "http://api.weatherapi.com/v1/current.json?key=" + API_KEY + "&q=" + city + "&aqi=yes";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new JSONObject(response.body());
    }

    public static JSONObject getForecast(String city, int days) throws Exception {
        String url = "http://api.weatherapi.com/v1/forecast.json?key=" + API_KEY
                + "&q=" + city + "&days=" + days + "&aqi=yes&alerts=no";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new JSONObject(response.body());
    }

    public static JSONObject getHistory(String city, LocalDate date) throws Exception {
        String url = "http://api.weatherapi.com/v1/history.json?key=" + API_KEY
                + "&q=" + city + "&dt=" + date.format(DateTimeFormatter.ISO_DATE) + "&aqi=yes";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new JSONObject(response.body());
    }


    public static JSONArray getLast7DaysHistory(String city) throws Exception {
        JSONArray historyArray = new JSONArray();
        for (int i = 1; i <= 7; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            JSONObject history = getHistory(city, date);
            JSONObject day = history.getJSONObject("forecast")
                    .getJSONArray("forecastday")
                    .getJSONObject(0)
                    .getJSONObject("day");
            JSONObject dayData = new JSONObject();
            dayData.put("date", date.toString());
            dayData.put("maxtemp_c", day.getDouble("maxtemp_c"));
            dayData.put("mintemp_c", day.getDouble("mintemp_c"));
            dayData.put("condition", day.getJSONObject("condition").getString("text"));
            historyArray.put(dayData);
        }
        return historyArray;
    }

    public static JSONArray getNext3DaysForecast(String city) throws Exception {
        JSONObject forecastJson = getForecast(city, 3);
        JSONArray forecastArray = new JSONArray();
        JSONArray days = forecastJson.getJSONObject("forecast").getJSONArray("forecastday");
        for (int i = 0; i < days.length(); i++) {
            JSONObject day = days.getJSONObject(i).getJSONObject("day");
            JSONObject dayData = new JSONObject();
            dayData.put("date", days.getJSONObject(i).getString("date"));
            dayData.put("maxtemp_c", day.getDouble("maxtemp_c"));
            dayData.put("mintemp_c", day.getDouble("mintemp_c"));
            dayData.put("condition", day.getJSONObject("condition").getString("text"));
            forecastArray.put(dayData);
        }
        return forecastArray;
    }
}
