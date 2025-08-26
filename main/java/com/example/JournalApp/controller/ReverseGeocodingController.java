package com.example.JournalApp.controller;

import com.example.JournalApp.apiResponse.WeatherResponse;
import com.example.JournalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
public class ReverseGeocodingController {

    @Autowired
    private WeatherService weatherService;

    @Value("${opencage.api.key")  // your OpenCage API key from application.properties
    private String openCageApiKey;

    @GetMapping
    public Map<String, Object> getWeatherForLocation(@RequestParam double lat, @RequestParam double lon) {
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        // OpenCage API URL with lat, lon and API key
//        String openCageUrl = String.format(
//                "https://api.opencagedata.com/geocode/v1/json?q=%f+%f&key=%s",
//                lat, lon, openCageApiKey
//        );
//
//        Map<String, Object> openCageResponse = restTemplate.getForObject(openCageUrl, Map.class);
//
//        System.out.println( "1 " + openCageResponse);
//
//        if (openCageResponse == null || !"OK".equals(openCageResponse.get("status").toString())) {
//            return Map.of("error", "Could not get location from OpenCage");
//        }
//
//        List<Map<String, Object>> results = (List<Map<String, Object>>) openCageResponse.get("results");
//        if (results.isEmpty()) {
//            return Map.of("error", "No location results found");
//        }
//
//        Map<String, Object> components = (Map<String, Object>) results.get(0).get("components");
//
//        System.out.println( " 2 " + components);
//        // Try to get village, town, city, or county in this priority
//        String locationName = (String) components.getOrDefault("village",
//                components.getOrDefault("town",
//                        components.getOrDefault("city",
//                                components.getOrDefault("county", "Unknown Location"))));

        String locationName = "guntur";
        // Call your weather service with the location name
        WeatherResponse weather = weatherService.getWeather(locationName);

        if (weather == null) {
            return Map.of("location", locationName, "error", "Weather data unavailable");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("location", locationName);
        result.put("cityName", weather.getName());
        result.put("temperature", weather.getMain().getTemp() - 273.15);  // Kelvin to Celsius
        result.put("feelsLike", weather.getMain().getFeelsLike() - 273.15);
        result.put("humidity", weather.getMain().getHumidity());
        result.put("windSpeed", weather.getWind().getSpeed());

        return result;
    }
}
