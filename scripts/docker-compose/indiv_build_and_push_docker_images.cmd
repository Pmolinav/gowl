@echo off
setlocal enabledelayedexpansion

if "%~1"=="" (
    echo [ERROR] No services specified. Usage: indiv_build_and_push_docker_images.cmd [ServiceName1] [ServiceName2] ...
    exit /b 1
)

echo -----------------------------------------
echo Starting requested Docker services...
echo -----------------------------------------

:loop
if "%~1"=="" goto end

set SERVICE=%~1

:: ---------- Public APIs ----------
if /i "%SERVICE%"=="AuthApi" (
    echo [AuthApi]
    cd /d "%~dp0..\..\apis\AuthApi"
    call docker build -t pablo7molina/authapi .
    call docker push pablo7molina/authapi
    echo [[32mAuthApi built and pushed OK[0m]
)

if /i "%SERVICE%"=="LeagueApi" (
    echo [LeagueApi]
    cd /d "%~dp0..\..\apis\LeagueApi"
    call docker build -t pablo7molina/leagueapi .
    call docker push pablo7molina/leagueapi
    echo [[32mLeagueApi built and pushed OK[0m]
)

if /i "%SERVICE%"=="PredictionApi" (
    echo [PredictionApi]
    cd /d "%~dp0..\..\apis\PredictionApi"
    call docker build -t pablo7molina/predictionapi .
    call docker push 8002:8002 pablo7molina/predictionapi
    echo [[32mPredictionApi built and pushed OK[0m]
)

:: ---------- BackOffice APIs ----------
if /i "%SERVICE%"=="LeaguesBOApi" (
    echo [LeaguesBOApi]
    cd /d "%~dp0..\..\backoffice\LeaguesBOApi"
    call docker build -t pablo7molina/leaguesboapi .
    call docker push pablo7molina/leaguesboapi
    echo [[32mLeaguesBOApi built and pushed OK[0m]
)

if /i "%SERVICE%"=="PredictionsBOApi" (
    echo [PredictionsBOApi]
    cd /d "%~dp0..\..\backoffice\PredictionsBOApi"
    call docker build -t pablo7molina/predictionsboapi .
    call docker push pablo7molina/predictionsboapi
    echo [[32mPredictionsBOApi built and pushed OK[0m]
)

if /i "%SERVICE%"=="UsersBOApi" (
    echo [UsersBOApi]
    cd /d "%~dp0..\..\backoffice\UsersBOApi"
    call docker build -t pablo7molina/usersboapi .
    call docker push pablo7molina/usersboapi
    echo [[32mUsersBOApi built and pushed OK[0m]
)

:: ---------- Services ----------
if /i "%SERVICE%"=="LeaguesService" (
    echo [LeaguesService]
    cd /d "%~dp0..\..\services\LeaguesService"
    call docker build -t pablo7molina/leaguesservice .
    call docker push pablo7molina/leaguesservice
    echo [[32mLeaguesService built and pushed OK[0m]
)

if /i "%SERVICE%"=="MatchDataSyncService" (
    echo [MatchDataSyncService]
    cd /d "%~dp0..\..\services\MatchDataSyncService"
    call docker build -t pablo7molina/matchdatasyncservice .
    call docker push pablo7molina/matchdatasyncservice
    echo [[32mMatchDataSyncService built and pushed OK[0m]
)

if /i "%SERVICE%"=="PredictionsService" (
    echo [PredictionsService]
    cd /d "%~dp0..\..\services\PredictionsService"
    call docker build -t pablo7molina/predictionsservice .
    call docker push pablo7molina/predictionsservice
    echo [[32mPredictionsService built and pushed OK[0m]
)

if /i "%SERVICE%"=="UsersService" (
    echo [UsersService]
    cd /d "%~dp0..\..\services\UsersService"
    call docker build -t pablo7molina/usersservice .
    call docker push pablo7molina/usersservice
    echo [[32mUsersService built and pushed OK[0m]
)

shift
goto loop

:end
echo -----------------------------------------
echo Selected services started successfully!
echo -----------------------------------------
endlocal
