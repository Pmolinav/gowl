@echo off
echo -----------------------------------------
echo Building and pushing Docker images...
echo -----------------------------------------

echo -----------------------------------------
echo Building and pushing Docker images for Public APIs...
echo -----------------------------------------

cd ../../apis/AuthApi
call docker build -t pablo7molina/authapi .
call docker push pablo7molina/authapi
echo [[32mAuthApi built and pushed OK[0m]

cd ../../apis/LeagueApi
call docker build -t pablo7molina/leagueapi .
call docker push pablo7molina/leagueapi
echo [[32mLeagueApi built and pushed OK[0m]

echo -----------------------------------------
echo Building and pushing Docker images for BackOffice APIs...
echo -----------------------------------------

cd ../../backoffice/UsersBOApi
call docker build -t pablo7molina/usersboapi .
call docker push pablo7molina/usersboapi
echo [[32mUsersBOApi built and pushed OK[0m]

cd ../../backoffice/LeaguesBOApi
call docker build -t pablo7molina/leaguesboapi .
call docker push pablo7molina/leaguesboapi
echo [[32mLeaguesBOApi built and pushed OK[0m]

echo -----------------------------------------
echo Building and pushing Docker images for Services...
echo -----------------------------------------

cd ../../services/UsersService
call docker build -t pablo7molina/usersservice .
call docker push pablo7molina/usersservice
echo [[32mUsersService built and pushed OK[0m]

cd ../../services/LeaguesService
call docker build -t pablo7molina/leaguesservice .
call docker push pablo7molina/leaguesservice
echo [[32mLeaguesService built and pushed OK[0m]

echo -----------------------------------------
echo Docker images built and pushed!
echo -----------------------------------------