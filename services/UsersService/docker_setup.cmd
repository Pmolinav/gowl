REM DEPRECATED
mvn clean package -DskipTests
REM Build image for the api service.
docker build -t usersservice-image .

REM Create container for built image.
docker run -d -p 8001:8001 --env-file .env --name usersservice --network spring -d usersservice-image