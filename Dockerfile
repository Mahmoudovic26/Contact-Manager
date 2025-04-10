FROM eclipse-temurin:24-jdk-alpine as build
WORKDIR /workspace/app

# Copy maven files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Give executable permissions to mvnw
RUN chmod +x ./mvnw

# Package the application
# RUN ./mvnw spring-boot:run

RUN ./mvnw clean package -DskipTests

# Create a smaller runtime image
FROM eclipse-temurin:24-jre-alpine
VOLUME /tmp
ARG JAR_FILE=/workspace/app/target/*.jar
COPY --from=build ${JAR_FILE} app.jar

# Run the application with a non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Hardcode the environment variables with your configuration
ENV SPRING_APPLICATION_NAME=CMA
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/cma
ENV SPRING_DATASOURCE_USERNAME=springstudent
ENV SPRING_DATASOURCE_PASSWORD=springstudent
ENV JWT_SECRET=VUzHaivpmW0iiwluk/Mvt6SyealK6WKlGFrDAqRCTYVcztAISJ9bJ4fsvh0yNvuNhD9C/VSob/GxyIu/I2TK7A==
ENV SPRING_MAIN_ALLOW-CIRCULAR-REFERENCES=true
ENV SPRING_JPA_HIBERNATE_DDL-AUTO=update
ENV LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_SECURITY=DEBUG

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]