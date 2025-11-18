package com.divertech.raxae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RaxaeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RaxaeApplication.class, args);
	}

}
