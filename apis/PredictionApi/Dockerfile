# Use a base image with JDK 21
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY ./target/PredictionApi-*.jar app.jar

ENV PORT=8009
EXPOSE $PORT

CMD ["java", "-jar", "app.jar"]
