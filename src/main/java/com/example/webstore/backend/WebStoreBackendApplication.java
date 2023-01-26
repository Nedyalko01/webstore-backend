package com.example.webstore.backend;

import com.github.javafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebStoreBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebStoreBackendApplication.class, args);
	}
		@Bean
		Faker faker() {
			return new Faker();
		}
	}

