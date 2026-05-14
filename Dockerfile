FROM maven:3.9.14-eclipse-temurin-21 AS build

ARG GITHUB_TOKEN
ARG GITHUB_USERNAME

COPY pom.xml .
COPY settings.xml /root/.m2/settings.xml

RUN sed -i "s/\${GITHUB_TOKEN}/${GITHUB_TOKEN}/g" /root/.m2/settings.xml && \
    sed -i "s/\${GITHUB_USERNAME}/${GITHUB_USERNAME}/g" /root/.m2/settings.xml

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:resolve

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy AS prod

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]


