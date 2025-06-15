REM DEPRECATED
mvn clean package -DskipTests
REM Build image for the api service.
docker build -t predictionsboapi-image .

REM Create container for built image.
docker run -d -p 8008:8008 --env-file .env --name predictionsboapi --network spring -d predictionsboapi-image