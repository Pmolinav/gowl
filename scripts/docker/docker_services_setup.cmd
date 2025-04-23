@echo off

REM Change to the service directory
cd ../../backoffice/UsersBOApi/

REM Build with Maven (skip tests)
call mvn clean package -DskipTests

REM Check if Maven build was successful
if %ERRORLEVEL% NEQ 0 (
    echo Error: Maven build failed for UsersBOApi. Check the errors and try again.
    exit /b 1
)
REM Docker login
call docker login

REM Build Docker image
call docker build -t pablo7molina/usersboapi .

REM Check if Docker image build was successful
if %ERRORLEVEL% NEQ 0 (
    echo Error: UsersBOApi Docker image build failed.
    exit /b 1
)

REM Docker image push to repository
call docker push pablo7molina/usersboapi

REM Run the container
call docker run -d -p 8002:8002 --env-file .env --network=spring --name usersboapi pablo7molina/usersboapi

REM Check if container execution was successful
if %ERRORLEVEL% NEQ 0 (
    echo Error: Failed to run UsersBOApi Docker container.
    exit /b 1
)

echo UsersBOApi has been built, Docker image has been created, and container has been started successfully!

REM Change to the service directory
cd ../../services/BookingsService/

REM Build with Maven (skip tests)
call mvn clean package -DskipTests

REM Check if Maven build was successful
if %ERRORLEVEL% NEQ 0 (
    echo Error: Maven build failed for BookingsService. Check the errors and try again.
    exit /b 1
)

REM Build Docker image
call docker build -t pablo7molina/bookingsservice .

REM Check if Docker image build was successful
if %ERRORLEVEL% NEQ 0 (
    echo Error: BookingsService Docker image build failed.
    exit /b 1
)

REM Docker image push to repository
call docker push pablo7molina/bookingsservice

REM Run the container
call docker run -d -p 8001:8001 --env-file .env --network=spring --name bookingsservice pablo7molina/bookingsservice

REM Check if container execution was successful
if %ERRORLEVEL% NEQ 0 (
    echo Error: Failed to run BookingsService Docker container.
    exit /b 1
)

echo BookingsService has been built, Docker image has been created, and container has been started successfully!
