package com.BTL_JAVA.BTL;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableFeignClients
@SpringBootApplication
public class BtlApplication {

	public static void main(String[] args) {
		SpringApplication.run(BtlApplication.class, args);
	}

    @Bean
    public CommandLineRunner commandLineRunner(BtlApplication application) {
        return runner -> {
            System.out.println("Welcome to Mini Lab !!!");
        };
    }

}
