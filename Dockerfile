# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Install certificates for TiDB/SSL connections
RUN apk add --no-cache ca-certificates

# === COPY JAR ===
# Matches <artifactId>treehugger</artifactId> and <version>0.0.1-SNAPSHOT</version> in pom.xml
COPY --from=build /app/target/treehugger-0.0.1-SNAPSHOT.jar app.jar

# === OPTIONAL: COPY SSL CERTIFICATE ===
# If TiDB requires a specific PEM file, place 'isrgrootx1.pem' in your project root
# and uncomment the line below:
# COPY isrgrootx1.pem /app/isrgrootx1.pem

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]