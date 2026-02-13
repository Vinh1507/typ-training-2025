package com.week2.phan1.non_db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.week2.phan1.non_db.db.MyDatabase.init;

@SpringBootApplication
public class NonDbApplication {

	public static void main(String[] args) {
		init();
		SpringApplication.run(NonDbApplication.class, args);
	}

}
