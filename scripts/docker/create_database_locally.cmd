REM DEPRECATED. Using docker-compose instead.
REM Create spring network to connect our containers.
docker network create spring

REM Create container for postgres database.
docker run --name postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=mysecretpassword -e POSTGRES_DB=gowl --network=spring -v data-postgres:/var/lib/postgresql/data -p 5432:5432 --restart=no -d postgres:15-alpine

REM Usuario: someUser
REM Password: $2a$10$pn85ACcwW6v74Kkt3pnPau7A4lv8N2d.fvwXuLsYanv07PzlXTu9S
REM INSERT INTO public.users (user_id, creation_date, email, modification_date, "name", "password", "role", username) VALUES(97, '2024-06-20 19:02:03.400', 'some@user.com', '2024-06-20 19:02:03.400', 'Some User', '$2a$10$IYiPMt0/CQOZTV1qxM30QOgOMGqTxssHonJ8KCsnuI66EASLpWI62', 'ADMIN', 'someUser');