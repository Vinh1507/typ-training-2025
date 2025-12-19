package com.project.TYP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// @SpringBootApplication
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class TypApplication {

	public static void main(String[] args) {
		SpringApplication.run(TypApplication.class, args);
	}

}
