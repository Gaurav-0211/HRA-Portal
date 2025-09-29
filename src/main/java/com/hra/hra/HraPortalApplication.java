package com.hra.hra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class HraPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(HraPortalApplication.class, args);
	}

}
