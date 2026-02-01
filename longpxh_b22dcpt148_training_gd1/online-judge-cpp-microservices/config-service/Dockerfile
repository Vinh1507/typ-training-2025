FROM eclipse-temurin:21-jre
LABEL authors="long"
WORKDIR /app
COPY target/config-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]