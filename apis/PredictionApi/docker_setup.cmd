REM DEPRECATED
mvn clean package -DskipTests
REM Build image for the api service.
docker build -t predictionapi-image .

REM Create container for built image.
docker run -d -p 8009:8009 --env-file .env --name predictionapi --network spring -d predictionapi-image