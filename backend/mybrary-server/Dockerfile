# --- Build Stage ---
FROM gradle:8.1.1-jdk17 AS build
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 의존성 캐싱 레이어: 소스 없이 dependencyResolution 먼저 수행
RUN ./gradlew dependencies --no-daemon || true

COPY src src
RUN ./gradlew clean build -x test --no-daemon

# --- Runtime Stage ---
FROM openjdk:17-slim AS runtime

RUN apt-get update && \
    apt-get install -y --no-install-recommends curl jq && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /app/build/libs/mybrary-server-0.0.1-SNAPSHOT.jar mybrary-server.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "mybrary-server.jar"]
