FROM eclipse-temurin:21-jre
LABEL authors="long"
WORKDIR /app
COPY submission-presentation/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
