@echo off

REM Change to the service directory
cd ../../backoffice/UsersBOApi/

REM Stop container.
call docker stop usersboapi

REM Check if Docker container stop was successful
if %ERRORLEVEL% NEQ 0 (
    echo Error: UsersBOApi Docker container stop failed.
    exit /b 1
)

REM Remove container.
call docker rm usersboapi

REM Check if container remove was successful
if %ERRORLEVEL% NEQ 0 (
    echo Error: Failed to remove the Docker container for UsersBOApi.
    exit /b 1
)

REM Remove image.
call docker rmi pablo7molina/usersboapi

if %ERRORLEVEL% NEQ 0 (
    echo Error: Failed to remove UsersBOApi Docker image.
    exit /b 1
)

echo UsersBOApi Docker container and image were removed successfully!

REM Change to the service directory
cd ../../services/UsersService/

REM Stop container.
call docker stop usersservice

REM Check if Docker container stop was successful
if %ERRORLEVEL% NEQ 0 (
    echo Error: UsersService Docker container stop failed.
    exit /b 1
)

REM Remove container.
call docker rm usersservice

REM Check if container remove was successful
if %ERRORLEVEL% NEQ 0 (
    echo Error: Failed to remove the Docker container for UsersService.
    exit /b 1
)

REM Remove image.
call docker rmi pablo7molina/usersservice

if %ERRORLEVEL% NEQ 0 (
    echo Error: Failed to remove UsersService Docker image.
    exit /b 1
)

echo UsersService Docker container and image were removed successfully!
