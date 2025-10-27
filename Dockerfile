FROM eclipse-temurin:21-jdk AS build

WORKDIR /src
COPY .mvn .mvn
COPY pom.xml .
COPY mvnw .
COPY src src

RUN ./mvnw package


FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /src/target/CineClub-0.0.1-SNAPSHOT.jar cineClub.jar

ENTRYPOINT [ "java", "-jar", "/app/cineClub.jar" ]