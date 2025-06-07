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


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;


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
