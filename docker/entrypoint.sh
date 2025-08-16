#!/bin/sh
export IS_DOCKER=true

echo "Waiting for PostgreSQL to become ready..."
while ! nc -z wallet-db 5432; do
  sleep 1
done

if [ -f "/app/.env" ]; then
    export $(grep -v '^#' /app/.env | xargs)
fi

exec java -jar app.jar \
    --spring.config.location=classpath:/,file:/app/config/application.yml