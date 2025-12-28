FROM gradle:9.2.1-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle :services:process-service:process-bootstrap:bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/services/process-service/process-bootstrap/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
