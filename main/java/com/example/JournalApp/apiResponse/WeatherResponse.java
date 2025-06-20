package com.example.JournalApp.apiResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    public Main main;
    public Wind wind;
    public String name;

    public class Main{
        public double temp;
        @JsonProperty("feels_like")
        public double feelsLike;
        public int humidity;
    }
    public class Wind{
        public double speed;
    }
}