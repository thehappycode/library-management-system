FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY infrastructure/service-discovery/pom.xml ./infrastructure/service-discovery/
COPY infrastructure/service-discovery/src ./infrastructure/service-discovery/src
RUN mvn clean package -pl infrastructure/service-discovery -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=builder /app/infrastructure/service-discovery/target/*.jar app.jar
EXPOSE 8761
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8761/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
