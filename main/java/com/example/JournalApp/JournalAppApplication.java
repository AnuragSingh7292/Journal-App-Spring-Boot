package com.example.JournalApp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
@EnableAsync
@SpringBootApplication
@EnableTransactionManagement
public class JournalAppApplication {

	public static void main(String[] args) {

	ConfigurableApplicationContext context = SpringApplication.run(JournalAppApplication.class, args);

	// its showing which database is active now like production database or development database that called is profiles
	ConfigurableEnvironment environment = context.getEnvironment();
		String[] profiles = environment.getActiveProfiles();
		if (profiles.length > 0) {
			System.out.println("✅ Active Profile: " + profiles[0]);
		} else {
			System.out.println("⚠️ No active profile set.");
		}
	}

	@Bean
	public PlatformTransactionManager add(MongoDatabaseFactory dbFactory){
		return new MongoTransactionManager(dbFactory);
	}
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Bean
	public CommandLineRunner checkMongoConnection(MongoTemplate mongoTemplate) {
		return args -> {
			try {
				mongoTemplate.getDb().listCollectionNames().first();
				System.out.println("✅ MongoDB is connected");
			} catch (Exception e) {
				System.err.println("❌ MongoDB connection failed: " + e.getMessage());
			}
		};
	}
}
