# Stage 1: Build the application using Maven
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application in a lightweight JRE environment
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

# This is the command that will be run to start your application
ENTRYPOINT ["java", "-jar", "app.jar"]