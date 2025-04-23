REM Compiling library
cd ../../libraries/users-lib

REM Build with Maven (skip tests)
call mvn clean package -DskipTests

REM Install lib with Maven (skip tests)
call mvn install -DskipTests

REM Building UsersBOApi image and push it to docker hub

cd ../../backoffice/UsersBOApi

REM Build with Maven (skip tests)
call mvn clean package -DskipTests

REM Build image
call docker build -t pablo7molina/usersboapi .

REM Push image
call docker push pablo7molina/usersboapi

REM Building BookingsService image and push it to docker hub
cd ../../services/BookingsService

REM Build with Maven (skip tests)
call mvn clean package -DskipTests

REM Build image
call docker build -t pablo7molina/bookingsservice .

REM Push image
call docker push pablo7molina/bookingsservice

REM Start Docker compose
docker-compose up -d