@echo off
setlocal enabledelayedexpansion

REM Save script base route first.
set BASE_DIR=%~dp0

echo -----------------------------------------
echo Building libraries...
echo -----------------------------------------

cd /d "%BASE_DIR%..\..\libraries\common-auth-lib"
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for common-auth-lib. See build.log && exit /b 1)
echo [[32mcommon-auth-lib OK[0m]
del build.log

cd /d "%BASE_DIR%..\..\libraries\leagues-lib"
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for leagues-lib. See build.log && exit /b 1)
echo [[32mleagues-lib OK[0m]
del build.log

cd /d "%BASE_DIR%..\..\libraries\predictions-lib"
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for predictions-lib. See build.log && exit /b 1)
echo [[32mpredictions-lib OK[0m]
del build.log

cd /d "%BASE_DIR%..\..\libraries\users-lib"
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for users-lib. See build.log && exit /b 1)
echo [[32musers-lib OK[0m]
del build.log

echo -----------------------------------------
echo Building Public APIs...
echo -----------------------------------------

cd /d "%BASE_DIR%..\..\apis\AuthApi"
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for AuthApi. See build.log && exit /b 1)
echo [[32mAuthApi OK[0m]
del build.log

cd /d "%BASE_DIR%..\..\apis\LeagueApi"
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for LeagueApi. See build.log && exit /b 1)
echo [[32mLeagueApi OK[0m]
del build.log

echo -----------------------------------------
echo Building BackOffice APIs...
echo -----------------------------------------

cd /d "%BASE_DIR%..\..\backoffice\LeaguesBOApi"
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for LeaguesBOApi. See build.log && exit /b 1)
echo [[32mLeaguesBOApi OK[0m]
del build.log

cd /d "%BASE_DIR%..\..\backoffice\PredictionsBOApi"
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for PredictionsBOApi. See build.log && exit /b 1)
echo [[32mPredictionsBOApi OK[0m]
del build.log

cd /d "%BASE_DIR%..\..\backoffice\UsersBOApi"
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for UsersBOApi. See build.log && exit /b 1)
echo [[32mUsersBOApi OK[0m]
del build.log

echo -----------------------------------------
echo Building Services...
echo -----------------------------------------

cd /d "%BASE_DIR%..\..\services\LeaguesService"
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for LeaguesService. See build.log && exit /b 1)
echo [[32mLeaguesService OK[0m]
del build.log

cd /d "%BASE_DIR%..\..\services\PredictionsService"
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for PredictionsService. See build.log && exit /b 1)
echo [[32mPredictionsService OK[0m]
del build.log

cd /d "%BASE_DIR%..\..\services\UsersService"
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for UsersService. See build.log && exit /b 1)
echo [[32mUsersService OK[0m]
del build.log

echo -----------------------------------------
echo Build completed!
echo -----------------------------------------

endlocal
