REM DEPRECATED
mvn clean package -DskipTests
REM Build image for the api service.
docker build -t matchdatasyncservice-image .

REM Create container for built image.
docker run -d -p 8010:8010 --env-file .env --name matchdatasyncservice --network spring -d matchdatasyncservice-image