REM DEPRECATED
mvn clean package -DskipTests
REM Build image for the api service.
docker build -t leaguesboapi-image .

REM Create container for built image.
docker run -d -p 8005:8005 --env-file .env --name leaguesboapi --network spring -d leaguesboapi-image