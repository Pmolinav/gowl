REM DEPRECATED
mvn clean package -DskipTests
REM Build image for the api service.
docker build -t predictionsservice-image .

REM Create container for built image.
docker run -d -p 8007:8007 --env-file .env --name predictionsservice --network spring -d predictionsservice-image