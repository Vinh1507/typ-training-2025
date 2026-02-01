package com.oj_cpp.config_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServiceApplication {

	public static void main(String[] args) {


        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
        System.out.println("user.dir = " + System.getProperty("user.dir"));

        System.out.println("GITLAB_TOKEN=" + dotenv.get("GITLAB_TOKEN"));

        dotenv.entries().forEach(e ->
                System.setProperty(e.getKey(), e.getValue())
        );



		SpringApplication.run(ConfigServiceApplication.class, args);
	}

}
