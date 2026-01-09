FROM gradle:9.2.1-jdk21 AS build
WORKDIR /app
COPY gradle gradle
COPY gradlew gradlew
COPY gradlew.bat gradlew.bat
COPY settings.gradle.kts build.gradle.kts ./

COPY process-core/build.gradle.kts process-core/build.gradle.kts
COPY process-application/build.gradle.kts process-application/build.gradle.kts
COPY process-adapters/grpc/build.gradle.kts process-adapters/grpc/build.gradle.kts
COPY process-adapters/rabbitmq/build.gradle.kts process-adapters/rabbitmq/build.gradle.kts
COPY process-adapters/excel/build.gradle.kts process-adapters/excel/build.gradle.kts
COPY process-adapters/mongo/build.gradle.kts process-adapters/mongo/build.gradle.kts
COPY process-bootstrap/build.gradle.kts process-bootstrap/build.gradle.kts

RUN --mount=type=cache,target=/home/gradle/.gradle \
    --mount=type=secret,id=gh_user \
    --mount=type=secret,id=gh_token \
    set -eu; \
    U="$(cat /run/secrets/gh_user)"; \
    T="$(cat /run/secrets/gh_token)"; \
    GITHUB_ACTOR="$U" GITHUB_TOKEN="$T" ./gradlew -q :process-bootstrap:dependencies --no-daemon || true


COPY process-core process-core
COPY process-application process-application
COPY process-adapters process-adapters
COPY process-bootstrap process-bootstrap

RUN --mount=type=cache,target=/home/gradle/.gradle \
    --mount=type=secret,id=gh_user \
    --mount=type=secret,id=gh_token \
    set -eu; \
    GH_USER="$(cat /run/secrets/gh_user)"; \
    GH_TOKEN="$(cat /run/secrets/gh_token)"; \
    GH_USER="$GH_USER" GH_TOKEN="$GH_TOKEN" ./gradlew :process-bootstrap:bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S app && adduser -S app -G app
USER app

COPY --from=build /app/process-bootstrap/build/libs/*SNAPSHOT.jar /app/app.jar

EXPOSE 8082

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:InitialRAMPercentage=50"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

