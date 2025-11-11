package com.proyect.cineclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CineClubApplication {

	public static void main(String[] args) {
		SpringApplication.run(CineClubApplication.class, args);
	}
}
