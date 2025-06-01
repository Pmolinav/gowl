# Use a base image with JDK 21
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY ./target/LeagueApi-*.jar app.jar

ENV PORT=8006
EXPOSE $PORT

CMD ["java", "-jar", "app.jar"]
