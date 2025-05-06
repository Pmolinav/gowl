REM DEPRECATED
mvn clean package -DskipTests
REM Build image for the api service.
docker build -t authapi-image .

REM Create container for built image.
docker run -d -p 8003:8003 --env-file .env --name authapi --network spring -d authapi-image