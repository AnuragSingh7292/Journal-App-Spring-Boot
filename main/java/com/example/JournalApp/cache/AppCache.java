package com.example.JournalApp.cache;

import com.example.JournalApp.JournalEntities.ConfigWeatherEntities;
import com.example.JournalApp.repository.configWeatherRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    @Autowired
    private configWeatherRepo repo;

    private final Map<String, String> APP_CACHE = new HashMap<>();

    @PostConstruct
    public void init() {
        List<ConfigWeatherEntities> list = repo.findAll();

        if (list.isEmpty()) {
            System.out.println("❌ No data found in MongoDB! Check your database.");
        } else {
//            System.out.println("✅ Loaded values into cache:");
            for (ConfigWeatherEntities var : list) {
                APP_CACHE.put(var.getKey(), var.getValue());
//                System.out.println("Key: " + var.getKey() + " | Value: " + var.getValue() );
            }
        }
    }

    public Map<String, String> getCache() {
        return APP_CACHE;
    }
}
