package com.notch.heist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HeistApplication {
	public static void main(String[] args) {
		SpringApplication.run(HeistApplication.class, args);
	}

}
