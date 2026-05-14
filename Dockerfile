# syntax=docker/dockerfile:1.7

FROM maven:3.9.14-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

RUN --mount=type=secret,id=maven_settings,target=/root/.m2/settings.xml \
    --mount=type=secret,id=github_token \
    export GITHUB_TOKEN=$(cat /run/secrets/github_token) && \
    mvn -B dependency:resolve

COPY src ./src

RUN --mount=type=secret,id=maven_settings,target=/root/.m2/settings.xml \
    --mount=type=secret,id=github_token \
    export GITHUB_TOKEN=$(cat /run/secrets/github_token) && \
    mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy AS prod

COPY --from=build /app/target/*.jar /app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]