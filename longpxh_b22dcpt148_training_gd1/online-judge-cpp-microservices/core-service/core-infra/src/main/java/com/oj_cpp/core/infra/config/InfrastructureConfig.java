package com.oj_cpp.core.infra.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.oj_cpp.core.infra.persistence.jpa.entity")
@EnableJpaRepositories(basePackages = "com.oj_cpp.core.infra.persistence.jpa.repository")
@ComponentScan(basePackages = "com.oj_cpp.core.infra")
public class InfrastructureConfig {
}
