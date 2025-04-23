# Use a base image with OpenJDK 11
FROM eclipse-temurin

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR construido desde el directorio target del backoffice
COPY ./target/BookingsService-*.jar app.jar

# Puerto configurado desde el archivo .env
ENV PORT=8001
EXPOSE $PORT

# Comando para ejecutar la aplicación Spring Boot cuando se inicie el contenedor
CMD ["java", "-jar", "app.jar"]
