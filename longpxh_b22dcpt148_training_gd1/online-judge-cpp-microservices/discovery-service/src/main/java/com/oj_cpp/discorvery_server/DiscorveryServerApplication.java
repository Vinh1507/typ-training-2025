package com.oj_cpp.discorvery_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscorveryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscorveryServerApplication.class, args);
	}

}
