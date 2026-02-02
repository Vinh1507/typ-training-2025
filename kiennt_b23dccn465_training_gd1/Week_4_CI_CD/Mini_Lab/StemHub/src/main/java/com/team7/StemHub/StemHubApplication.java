package com.team7.StemHub;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StemHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(StemHubApplication.class, args);
	}

    @Bean
    public CommandLineRunner commandLineRunner(StemHubApplication application) {
        return runner -> {
            System.out.println("Welcome to StemHub !!!");
        };
    }
}
