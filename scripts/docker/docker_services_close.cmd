@echo off

REM Change to the service directory
cd ../../backoffice/ConfigurationBOApi/

REM Stop container.
call docker stop configurationboapi

REM Check if Docker container stop was successful
if %ERRORLEVEL% NEQ 0 (
    echo Error: ConfigurationBOApi Docker container stop failed.
    exit /b 1
)

REM Remove container.
call docker rm configurationboapi

REM Check if container remove was successful
if %ERRORLEVEL% NEQ 0 (
    echo Error: Failed to remove the Docker container for ConfigurationBOApi.
    exit /b 1
)

REM Remove image.
call docker rmi pablo7molina/configurationboapi

if %ERRORLEVEL% NEQ 0 (
    echo Error: Failed to remove ConfigurationBOApi Docker image.
    exit /b 1
)

echo ConfigurationBOApi Docker container and image were removed successfully!

REM Change to the service directory
cd ../../services/BookingsService/

REM Stop container.
call docker stop bookingsservice

REM Check if Docker container stop was successful
if %ERRORLEVEL% NEQ 0 (
    echo Error: BookingsService Docker container stop failed.
    exit /b 1
)

REM Remove container.
call docker rm bookingsservice

REM Check if container remove was successful
if %ERRORLEVEL% NEQ 0 (
    echo Error: Failed to remove the Docker container for BookingsService.
    exit /b 1
)

REM Remove image.
call docker rmi pablo7molina/bookingsservice

if %ERRORLEVEL% NEQ 0 (
    echo Error: Failed to remove BookingsService Docker image.
    exit /b 1
)

echo BookingsService Docker container and image were removed successfully!
