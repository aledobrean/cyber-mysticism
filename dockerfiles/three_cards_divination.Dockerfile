# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

COPY ../.mvn .mvn
COPY ../mvnw pom.xml ./
COPY ../three-cards-divination/pom.xml ./three-cards-divination/
RUN ./mvnw dependency:resolve
COPY ../three-cards-divination/src ./three-cards-divination/src
RUN ./mvnw package
COPY ../three-cards-divination/target ./three-cards-divination/target

# Application stage
FROM eclipse-temurin:21-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

COPY --from=build /app/three-cards-divination/target/*.jar ./app.jar
CMD ["java", "-jar", "./app.jar"]