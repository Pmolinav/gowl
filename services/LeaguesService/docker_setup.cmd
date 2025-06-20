REM DEPRECATED
mvn clean package -DskipTests
REM Build image for the api service.
docker build -t leaguesservice-image .

REM Create container for built image.
docker run -d -p 8004:8004 --env-file .env --name leaguesservice --network spring -d leaguesservice-image