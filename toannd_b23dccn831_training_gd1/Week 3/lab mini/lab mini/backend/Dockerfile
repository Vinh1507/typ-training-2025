# Stage 1: Build
FROM  eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar -x test --no-daemon

# Stage 2: Run
FROM eclipse-temurin:17-jre
WORKDIR /run
COPY --from=build /app/build/libs/bookshop.jar /run/bookshop.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/run/bookshop.jar"]
