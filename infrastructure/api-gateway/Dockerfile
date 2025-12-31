FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY common ./common
COPY infrastructure/api-gateway/pom.xml ./infrastructure/api-gateway/
COPY infrastructure/api-gateway/src ./infrastructure/api-gateway/src
RUN mvn clean package -pl infrastructure/api-gateway -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=builder /app/infrastructure/api-gateway/target/*.jar app.jar
EXPOSE 8000
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8000/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
