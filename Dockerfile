# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Step 1: Sirf pom.xml copy karein aur dependencies download karein (Caching ke liye)
COPY pom.xml .
RUN mvn dependency:go-offline

# Step 2: Apna code copy karein aur build karein (Tests skip karke)
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true

# Stage 2: Run the application (Lighter aur secure JRE image)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
