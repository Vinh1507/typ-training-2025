FROM eclipse-temurin:21-jre
LABEL authors="long"

# Install g++ and build tools
RUN apt-get update && \
    apt-get install -y build-essential g++ && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY judge-presentation/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
