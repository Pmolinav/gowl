@echo off

echo DEPRECATED: This script is deprecated and will be removed in the future.
echo Using docker-compose instead.

REM Create spring network to connect our containers.
docker network create spring

REM Create container for postgres databases.
docker run --name postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=mysecretpassword -e POSTGRES_DB=users --network=spring -v data-postgres:/var/lib/postgresql/data -p 5432:5432 --restart=no -d postgres:15-alpine
docker run --name postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=mysecretpassword -e POSTGRES_DB=leagues --network=spring -v data-postgres:/var/lib/postgresql/data -p 5432:5432 --restart=no -d postgres:15-alpine
docker run --name postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=mysecretpassword -e POSTGRES_DB=predictions --network=spring -v data-postgres:/var/lib/postgresql/data -p 5432:5432 --restart=no -d postgres:15-alpine

REM Usuario: someUser
REM Password: $2a$10$pn85ACcwW6v74Kkt3pnPau7A4lv8N2d.fvwXuLsYanv07PzlXTu9S
REM INSERT INTO public.users (user_id, creation_date, email, modification_date, "name", "password", username) VALUES(97, '2024-06-20 19:02:03.400', 'some@user.com', '2024-06-20 19:02:03.400', 'Some User', '$2a$10$OdgftIlQfdZH5vHC2gLjDeS9cdtp..bGZn1Yw79LcRAyvqo3mua6C', 'someUser');