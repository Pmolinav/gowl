REM End docker-compose and volumes
docker-compose down -v

REM Remove unused images that do not have name and could have been created in the process
docker image prune -f --filter "dangling=true"