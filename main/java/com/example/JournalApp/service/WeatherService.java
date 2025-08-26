package com.example.JournalApp.service;

import com.example.JournalApp.apiResponse.WeatherResponse;
import com.example.JournalApp.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {

    // hardcoded
//    private static final String apiKey = "9ea33fc4b42f6101f239144bbcf46636";
//    @Value("${weather.api.key}") // its coming from the application.yml
//    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    // manualy doing
//    public WeatherResponse getWeather1(String city) {
//        String apiURL = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
//
//        ResponseEntity<WeatherResponse> response =  restTemplate.exchange(apiURL, HttpMethod.GET, null, WeatherResponse.class);
//        WeatherResponse body =  response.getBody();
//        return body;
//    }

//    by using the  app cache memory
    public WeatherResponse getWeather(String city) {

        String baseAPI = appCache.getCache().get("Weather_API");
        String apiKey = appCache.getCache().get("Weather_apiKey");

        if (baseAPI == null) {
            throw new RuntimeException("Weather_api key not found in cache");
        }
        String finalAPI = baseAPI.replace("<city>", city).replace("<apiKey>", apiKey).trim();
//        System.out.println("Final API URL: " + finalAPI);

        // Normalize whitespace and encoding
        finalAPI = finalAPI.replaceAll("\\s+", "");  // Remove hidden spaces
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse body = response.getBody();
        return body;
    }
}
