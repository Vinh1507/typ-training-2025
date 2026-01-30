
# Stage 1: Build
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew bootJar -x test --no-daemon

# Stage 2: Run
FROM eclipse-temurin:17-jre
WORKDIR /run
COPY --from=build /app/build/libs/*.jar /run/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/run/app.jar"]
