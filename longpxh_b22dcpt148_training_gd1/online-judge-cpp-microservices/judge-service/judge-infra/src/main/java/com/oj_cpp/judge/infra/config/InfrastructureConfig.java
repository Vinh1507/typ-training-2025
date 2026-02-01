package com.oj_cpp.judge.infra.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
// @EntityScan(basePackages = "com.oj_cpp.judge.infra.persistence.jpa.entity")
// @EnableJpaRepositories(basePackages = "com.oj_cpp.judge.infra.persistence.jpa.repository")
@ComponentScan(basePackages = "com.oj_cpp.judge.infra")
public class InfrastructureConfig {
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
