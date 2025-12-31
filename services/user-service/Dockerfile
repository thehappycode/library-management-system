# Multi-stage build for optimized image size
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copy parent pom and common libraries first for better caching
COPY pom.xml .
COPY common ./common

# Copy service-specific files
COPY services/user-service/pom.xml ./services/user-service/
COPY services/user-service/src ./services/user-service/src

# Build the application
RUN mvn clean package -pl services/user-service -am -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Add non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy built artifact from builder stage
COPY --from=builder /app/services/user-service/target/*.jar app.jar

# Expose service port
EXPOSE 8082

# Health check
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8082/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
