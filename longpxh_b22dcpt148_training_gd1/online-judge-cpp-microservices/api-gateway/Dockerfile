FROM eclipse-temurin:21-jre
LABEL authors="long"
WORKDIR /app
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
