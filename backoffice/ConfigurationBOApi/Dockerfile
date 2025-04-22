# Use a base image with OpenJDK 11
FROM openjdk:17-alpine

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR construido desde el directorio target del backoffice
COPY ./target/ConfigurationBOApi-*.jar app.jar

# Puerto configurado desde el archivo .env
ENV PORT=8002
EXPOSE $PORT

# Comando para ejecutar la aplicación Spring Boot cuando se inicie el contenedor
CMD ["java", "-jar", "app.jar"]
