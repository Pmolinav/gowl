# Use a base image with JDK 21
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY ./target/PredictionsService-*.jar app.jar

ENV PORT=8007
EXPOSE $PORT

CMD ["java", "-jar", "app.jar"]
