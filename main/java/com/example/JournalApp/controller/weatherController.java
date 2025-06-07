package com.example.JournalApp.controller;

import com.example.JournalApp.apiResponse.WeatherResponse;
import com.example.JournalApp.repository.userRepo;
import com.example.JournalApp.service.UserService;
import com.example.JournalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
public class weatherController {


    @Autowired
    private UserService userService;
    @Autowired
    private userRepo userRepo;
    @Autowired
    private WeatherService weatherService;


    @GetMapping
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        WeatherResponse response = weatherService.getWeather("delhi");
        StringBuilder greeting = new StringBuilder();

        if (response != null) {
            double tempCelsius = response.getMain().temp - 273.15;
            double feelsLikeCelsius = response.getMain().feelsLike - 273.15;

            greeting.append("Weather feels like: ")
                    .append(capitalize(response.name)).append(" | ")
                    .append("Wind Speed: ").append(response.getWind().speed).append(" km/h | ")
                    .append("Temperature: ").append(String.format("%.2f", tempCelsius)).append("°C | ")
                    .append("Feels Like: ").append(String.format("%.2f", feelsLikeCelsius)).append("°C | ")
                    .append("Humidity: ").append(response.getMain().humidity).append("%");
        } else {
            greeting.append("Weather information is not available at the moment.");
        }

        return "Hi " + username + "! " + greeting;
    }

    private String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

}
