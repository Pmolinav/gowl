REM DEPRECATED. Using docker-compose instead.
REM Create spring (if not exists) network to connect our containers.
docker network create spring

REM Zookeeper
call docker run -d --name zookeeper -p 2181:2181 --network spring -e ALLOW_ANONYMOUS_LOGIN=yes bitnami/zookeeper:latest

REM Kafka run
call docker run -d --name kafka -p 9092:9092 --network spring --env KAFKA_BROKER_ID=1 --env KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 --env KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 bitnami/kafka:latest
