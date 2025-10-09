FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /workspace

# copy maven wrapper and pom first to leverage layer caching
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Download dependencies
RUN mvn -B -ntp -f pom.xml dependency:go-offline

# copy sources and build
COPY src src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY --from=build /workspace/target/*.jar /app/app.jar

EXPOSE 8443

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
