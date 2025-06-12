@echo off
echo -----------------------------------------
echo Building libraries...
echo -----------------------------------------

cd ../../libraries/common-auth-lib
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for common-auth-lib. See build.log && exit /b 1)
echo [[32mcommon-auth-lib OK[0m]
del build.log

cd ../../libraries/users-lib
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for users-lib. See build.log && exit /b 1)
echo [[32musers-lib OK[0m]
del build.log

cd ../../libraries/leagues-lib
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for leagues-lib. See build.log && exit /b 1)
echo [[32mleagues-lib OK[0m]
del build.log

cd ../../libraries/predictions-lib
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for predictions-lib. See build.log && exit /b 1)
echo [[32mpredictions-lib OK[0m]
del build.log

echo -----------------------------------------
echo Building Public APIs...
echo -----------------------------------------

cd ../../apis/AuthApi
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for AuthApi. See build.log && exit /b 1)
echo [[32mAuthApi OK[0m]
del build.log

cd ../../apis/LeagueApi
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for LeagueApi. See build.log && exit /b 1)
echo [[32mLeagueApi OK[0m]
del build.log

echo -----------------------------------------
echo Building BackOffice APIs...
echo -----------------------------------------

cd ../../backoffice/UsersBOApi
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for UsersBOApi. See build.log && exit /b 1)
echo [[32mUsersBOApi-lib OK[0m]
del build.log

cd ../../backoffice/LeaguesBOApi
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for LeaguesBOApi. See build.log && exit /b 1)
echo [[32mLeaguesBOApi OK[0m]
del build.log

echo -----------------------------------------
echo Building Services...
echo -----------------------------------------

cd ../../services/UsersService
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for UsersService. See build.log && exit /b 1)
echo [[32mUsersService OK[0m]
del build.log

cd ../../services/LeaguesService
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for LeaguesService. See build.log && exit /b 1)
echo [[32mLeaguesService OK[0m]
del build.log

cd ../../services/PredictionsService
call mvn clean install -DskipTests > build.log 2>&1 || (echo Build failed for PredictionsService. See build.log && exit /b 1)
echo [[32mPredictionsService OK[0m]
del build.log

echo -----------------------------------------
echo Build completed!
echo -----------------------------------------
