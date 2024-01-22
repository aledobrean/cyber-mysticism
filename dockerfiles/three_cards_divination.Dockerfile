# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

COPY --chown=spring:spring .mvn .mvn
COPY --chown=spring:spring mvnw pom.xml ./
COPY --chown=spring:spring three-cards-divination/pom.xml ./three-cards-divination/
RUN ./mvnw dependency:resolve
COPY --chown=spring:spring three-cards-divination/src ./three-cards-divination/src
RUN ./mvnw package -DskipTests
COPY --chown=spring:spring three-cards-divination/target ./three-cards-divination/target

# Application stage
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

COPY --from=build /app/three-cards-divination/target/*.jar ./app.jar
CMD ["java", "-jar", "./app.jar"]