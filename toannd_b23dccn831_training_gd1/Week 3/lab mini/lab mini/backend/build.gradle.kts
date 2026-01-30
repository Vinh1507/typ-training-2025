plugins {
	java
	id("org.springframework.boot") version "3.3.2"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "ptit.edu"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
	implementation("com.turkraft.springfilter:jpa:3.1.7")
	implementation("org.modelmapper:modelmapper:3.1.1")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    implementation("com.cloudinary:cloudinary-http44:1.39.0")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("redis.clients:jedis:5.1.4")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("net.coobird:thumbnailator:0.4.20")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	archiveFileName.set("bookshop.jar")
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}