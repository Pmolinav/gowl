@echo off
setlocal enabledelayedexpansion

if "%~1"=="" (
    echo [ERROR] No components specified. Usage: indiv_build_services.cmd [Name1] [Name2] ...
    exit /b 1
)

set BASE_DIR=%~dp0

echo -----------------------------------------
echo Selective Build Script Started
echo -----------------------------------------

:loop
if "%~1"=="" goto end

set COMPONENT=%~1

:: ----------------- Libraries -----------------
if /i "%COMPONENT%"=="common-auth-lib" (
    cd /d "%BASE_DIR%..\..\libraries\common-auth-lib"
    call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for common-auth-lib. See build.log && exit /b 1)
    echo [[32mcommon-auth-lib OK[0m]
    del build.log
)

if /i "%COMPONENT%"=="leagues-lib" (
    cd /d "%BASE_DIR%..\..\libraries\leagues-lib"
    call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for leagues-lib. See build.log && exit /b 1)
    echo [[32mleagues-lib OK[0m]
    del build.log
)

if /i "%COMPONENT%"=="predictions-lib" (
    cd /d "%BASE_DIR%..\..\libraries\predictions-lib"
    call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for predictions-lib. See build.log && exit /b 1)
    echo [[32mpredictions-lib OK[0m]
    del build.log
)

if /i "%COMPONENT%"=="users-lib" (
    cd /d "%BASE_DIR%..\..\libraries\users-lib"
    call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for users-lib. See build.log && exit /b 1)
    echo [[32musers-lib OK[0m]
    del build.log
)

:: ----------------- Public APIs -----------------
if /i "%COMPONENT%"=="authapi" (
    cd /d "%BASE_DIR%..\..\apis\AuthApi"
    call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for AuthApi. See build.log && exit /b 1)
    echo [[32mAuthApi OK[0m]
    del build.log
)

if /i "%COMPONENT%"=="leagueapi" (
    cd /d "%BASE_DIR%..\..\apis\LeagueApi"
    call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for LeagueApi. See build.log && exit /b 1)
    echo [[32mLeagueApi OK[0m]
    del build.log
)

if /i "%COMPONENT%"=="predictionapi" (
    cd /d "%BASE_DIR%..\..\apis\PredictionApi"
    call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for PredictionApi. See build.log && exit /b 1)
    echo [[32mPredictionApi OK[0m]
    del build.log
)

:: ----------------- BackOffice APIs -----------------
if /i "%COMPONENT%"=="leaguesboapi" (
    cd /d "%BASE_DIR%..\..\backoffice\LeaguesBOApi"
    call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for LeaguesBOApi. See build.log && exit /b 1)
    echo [[32mLeaguesBOApi OK[0m]
    del build.log
)

if /i "%COMPONENT%"=="predictionsboapi" (
    cd /d "%BASE_DIR%..\..\backoffice\PredictionsBOApi"
    call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for PredictionsBOApi. See build.log && exit /b 1)
    echo [[32mPredictionsBOApi OK[0m]
    del build.log
)

if /i "%COMPONENT%"=="usersboapi" (
    cd /d "%BASE_DIR%..\..\backoffice\UsersBOApi"
    call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for UsersBOApi. See build.log && exit /b 1)
    echo [[32mUsersBOApi OK[0m]
    del build.log
)

:: ----------------- Services -----------------
if /i "%COMPONENT%"=="leaguesservice" (
    cd /d "%BASE_DIR%..\..\services\LeaguesService"
    call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for LeaguesService. See build.log && exit /b 1)
    echo [[32mLeaguesService OK[0m]
    del build.log
)

if /i "%COMPONENT%"=="matchdatasyncservice" (
    cd /d "%BASE_DIR%..\..\services\MatchDataSyncService"
    call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for MatchDataSyncService. See build.log && exit /b 1)
    echo [[32mMatchDataSyncService OK[0m]
    del build.log
)

if /i "%COMPONENT%"=="predictionsservice" (
    cd /d "%BASE_DIR%..\..\services\PredictionsService"
    call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for PredictionsService. See build.log && exit /b 1)
    echo [[32mPredictionsService OK[0m]
    del build.log
)

if /i "%COMPONENT%"=="usersservice" (
    cd /d "%BASE_DIR%..\..\services\UsersService"
    call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for UsersService. See build.log && exit /b 1)
    echo [[32mUsersService OK[0m]
    del build.log
)

shift
goto loop

:end
echo -----------------------------------------
echo Selected builds completed successfully!
echo -----------------------------------------
endlocal
