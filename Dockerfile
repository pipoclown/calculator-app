# Dockerfile
FROM eclipse-temurin:21-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENV SERVER_PORT=8080
ENTRYPOINT ["java", "-jar", "/app.jar"]

