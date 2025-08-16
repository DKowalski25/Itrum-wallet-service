FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

COPY gradlew .
COPY build.gradle.kts ./
COPY gradle ./gradle

RUN ./gradlew build -x test --dry-run

COPY . .

RUN ./gradlew build -x test

FROM eclipse-temurin:17-jre
WORKDIR /app

RUN mkdir -p /app/logs

COPY --from=builder /app/build/libs/*.jar app.jar
COPY --from=builder /app/.env .

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]