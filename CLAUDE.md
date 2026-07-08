# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Mybrary is a Korean book social network app — "connecting people through books." It consists of a Flutter mobile frontend and a Spring Boot MSA (microservices architecture) backend.

> Note: As of September 2023 the services were split into separate repositories. This monorepo still contains the original combined codebase.

## Architecture

### Backend (MSA — Spring Boot 3, Java 17, Spring Cloud 2022.0.3)

All requests from the mobile app flow through a single entry point:

```
Flutter App → API Gateway → Eureka (service discovery) → user-service / book-service
                                 ↑
                           Config Server (centralizes application.yml)
```

| Service | Port | Role |
|---|---|---|
| `config-server` | — | Centralized config via Spring Cloud Config; services fetch config on startup via `bootstrap.yml` |
| `eureka-server` | — | Netflix Eureka service registry |
| `apigateway-server` | — | Spring Cloud Gateway; JWT validation in `AuthorizationHeaderFilter`, routes to downstream services |
| `user-service` | — | User accounts, OAuth2 social login (Kakao/Naver/Google), follows, interests; Spring Security + JWT + Redis |
| `book-service` | — | Book search (Kakao/Aladin APIs), MyBook CRUD, reviews, meaning tags, ranking; Resilience4j circuit breaker |

Each backend service under `backend/<service-name>/` is an independent Gradle project with its own `gradlew`.

The API Gateway validates JWTs against Redis (blacklist of logged-out tokens) before forwarding. User identity is propagated downstream via headers (`userId`, etc.) rather than re-validating JWT in each service.

Inter-service calls (e.g., book-service → user-service) use **Spring Cloud OpenFeign** (`client/user/api/UserServiceClient.java`).

### Frontend (Flutter, Dart)

State management: **Riverpod** (`flutter_riverpod`). HTTP client: **Dio** with a custom auth interceptor (`utils/dios/auth_dio.dart`) that handles JWT refresh.

```
lib/
  data/
    datasource/    # API calls (Dio)
    model/         # Response DTOs
    repository/    # Glue between datasource and UI
  provider/        # Riverpod / shared state (UserState via SharedPreferences)
  res/constants/   # Colors, styles, enums, config keys
  ui/              # Feature screens, each with components/ subfolder
  utils/           # Dio services, logic helpers, animations
  main.dart        # App entry, route definitions, init logic
```

All API endpoints are defined in a single enum in `frontend/lib/data/network/api.dart` pointing to `https://mybrary.kr` (production). Feature flags / env values loaded via `.env` (flutter_dotenv).

## Build & Test Commands

### Backend — run from each service directory

```bash
# Run tests
cd backend/user-service && ./gradlew test
cd backend/book-service && ./gradlew test

# Build (skip tests)
./gradlew clean build -x test

# Run a single test class
./gradlew test --tests "kr.mybrary.userservice.user.domain.UserServiceImplTest"

# Generate API docs (Spring REST Docs + Swagger UI JSON)
./gradlew openapi3

# Clean QueryDSL-generated sources
./gradlew clean   # deletes src/main/generated
```

> `bootstrap.yml` controls `spring.profiles.active` (`test` vs `prod`). In CI this is overridden by the workflow; locally set it to `test` to skip Config Server lookup.

### Frontend

```bash
cd frontend

# Install dependencies
flutter pub get

# Run on connected device / simulator
flutter run

# Run tests
flutter test

# Run a single test file
flutter test test/utils/some_test.dart

# Build iOS release
flutter build ios --release

# Build Android release
flutter build apk --release
```

## Key Patterns

- **Test fixtures**: Each domain has `*Fixture.java` (entity builders) and `*DtoTestData.java` (DTO builders) in `src/test/java/.../<domain>/`. Controller tests use `@WebMvcTest` with MockMvc + Spring REST Docs to generate API spec snippets.
- **QueryDSL**: Generated Q-classes live in `src/main/generated/`. They are deleted on `./gradlew clean`.
- **MapStruct**: Used for entity↔DTO mapping. Requires both `mapstruct-processor` and `lombok-mapstruct-binding` annotation processors in the correct order in `build.gradle`.
- **API docs**: Tests produce OpenAPI 3 JSON via `openapi3` task → deployed as static files at `build/resources/main/static/docs/`.

## CI/CD

GitHub Actions workflows in `.github/workflows/` trigger on pushes to `master` that touch `backend/<service-name>/**`:
1. Run Gradle tests
2. Build Docker image (`Dockerfile` in each service root)
3. Push to AWS ECR
4. Deploy to ECS Fargate
