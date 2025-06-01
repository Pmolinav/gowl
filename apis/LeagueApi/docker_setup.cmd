REM DEPRECATED
mvn clean package -DskipTests
REM Build image for the api service.
docker build -t leagueapi-image .

REM Create container for built image.
docker run -d -p 8006:8006 --env-file .env --name leagueapi --network spring -d leagueapi-image