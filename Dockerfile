FROM eclipse-temurin:17-jdk as builder
WORKDIR /app

COPY build/libs/wallet-service-1.0.jar app.jar

VOLUME /app/config

COPY docker/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]