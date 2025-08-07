package com.karan.wbtracker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WbtrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WbtrackerApplication.class, args);
	}

	@Bean
	public CommandLineRunner connectionTest(MongoTemplate mongoTemplate) {
		return args -> {
			try {
				// This command will check if a connection can be established
				mongoTemplate.getCollectionNames();
				System.out.println("✅ MongoDB connection successful!");
			} catch (Exception e) {
				System.err.println("❌ MongoDB connection failed: " + e.getMessage());
			}
		};
	}

}
