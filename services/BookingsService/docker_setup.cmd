REM DEPRECATED
mvn clean package -DskipTests
REM Build image for the api service.
docker build -t configurationboapi-image .

REM Create container for built image.
docker run -d -p 8002:8002 --env-file .env --name configurationboapi --network spring -d configurationboapi-image