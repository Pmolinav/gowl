REM Compiling users-lib library
cd ../../libraries/users-lib

REM Build with Maven (skip tests)
call mvn clean package -DskipTests

REM Install lib with Maven (skip tests)
call mvn install -DskipTests

REM Compiling common-auth-lib library
cd ../../libraries/common-auth-lib

REM Build with Maven (skip tests)
call mvn clean package -DskipTests

REM Install lib with Maven (skip tests)
call mvn install -DskipTests

REM Building AuthApi image and push it to docker hub

cd ../../apis/AuthApi

REM Build with Maven (skip tests)
call mvn clean package -DskipTests

REM Build image
call docker build -t pablo7molina/authapi .

REM Push image
call docker push pablo7molina/authapi

REM Building UsersBOApi image and push it to docker hub

cd ../../backoffice/UsersBOApi

REM Build with Maven (skip tests)
call mvn clean package -DskipTests

REM Build image
call docker build -t pablo7molina/usersboapi .

REM Push image
call docker push pablo7molina/usersboapi

REM Building UsersService image and push it to docker hub
cd ../../services/UsersService

REM Build with Maven (skip tests)
call mvn clean package -DskipTests

REM Build image
call docker build -t pablo7molina/usersservice .

REM Push image
call docker push pablo7molina/usersservice

REM Start Docker compose
docker-compose up -d