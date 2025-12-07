package com.oj.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.oj.platform.libs.env.EnvLoader;

@SpringBootApplication(scanBasePackages = {
        "com.oj.start",
        "com.oj.application",
        "com.oj.platform"
})
@EnableJpaRepositories(basePackages = "com.oj.platform")
@EntityScan(basePackages = "com.oj.platform")
public class StartApplication {

    public static void main(String[] args) {
        // Try loading .env from current directory, then parent directory

        EnvLoader.load(".", ".env");

        // Get active profile from environment or system property
        String profile = System.getProperty("OJ_SPRING_PROFILES_ACTIVE",
                System.getProperty("spring.profiles.active", "local"));
        System.out.println("[dotenv] Active profile: " + profile);

        // Debug important properties
        System.out.println("[dotenv] Final System Properties (selected):");
        System.out.println("OJ_SPRING_PROFILES_ACTIVE=" + profile);
        System.out.println("OJ_DB_URL=" + System.getProperty("OJ_DB_URL"));
        System.out.println("OJ_DB_USERNAME=" + System.getProperty("OJ_DB_USERNAME"));
        System.out.println("OJ_SERVER_PORT=" + System.getProperty("OJ_SERVER_PORT", "8080"));


        SpringApplication.run(StartApplication.class, args);
    }

}
