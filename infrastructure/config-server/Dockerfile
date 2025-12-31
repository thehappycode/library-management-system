FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY infrastructure/config-server/pom.xml ./infrastructure/config-server/
COPY infrastructure/config-server/src ./infrastructure/config-server/src
RUN mvn clean package -pl infrastructure/config-server -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=builder /app/infrastructure/config-server/target/*.jar app.jar
EXPOSE 8888
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8888/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
