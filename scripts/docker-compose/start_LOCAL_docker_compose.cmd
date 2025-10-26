REM Start docker-compose with no obligation of building images before
docker-compose -f ../../docker-compose.yml -f ../../docker-compose.local.yml up --build -d