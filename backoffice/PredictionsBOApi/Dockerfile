# Use a base image with JDK 21
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY ./target/PredictionsBOApi-*.jar app.jar

ENV PORT=8008
EXPOSE $PORT

CMD ["java", "-jar", "app.jar"]
