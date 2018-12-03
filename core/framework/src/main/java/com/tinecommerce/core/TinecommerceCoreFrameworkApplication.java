package com.tinecommerce.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TinecommerceCoreFrameworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(TinecommerceCoreFrameworkApplication.class, args);
	}
}
