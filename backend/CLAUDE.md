# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Spring Boot 3 / Java 17 MSA 백엔드. 다섯 개의 독립 Gradle 프로젝트로 구성된다.

| 디렉토리 | 역할 |
|---|---|
| `config-server` | Spring Cloud Config — 전체 서비스의 `application.yml`을 중앙 관리 |
| `eureka-server` | Netflix Eureka 서비스 레지스트리 |
| `apigateway-server` | Spring Cloud Gateway — JWT 검증(`AuthorizationHeaderFilter`), 로드밸런서 라우팅 |
| `user-service` | 회원가입/로그인, OAuth2 소셜 로그인(Kakao/Naver/Google), 팔로우, 관심사, 프로필 이미지(S3) |
| `book-service` | 도서 검색(Kakao/Aladin API), MyBook CRUD, 리뷰, 태그, 관심도서, 인기 검색어(Redis) |

요청 흐름: `Mobile → API Gateway(8000) → Eureka → user-service / book-service`

---

## Build & Test Commands

각 서비스는 독립 Gradle wrapper를 사용한다. **반드시 해당 서비스 디렉토리 안에서 실행**한다.

```bash
# 테스트 실행
cd backend/user-service && ./gradlew test
cd backend/book-service && ./gradlew test

# 단일 테스트 클래스 실행
./gradlew test --tests "kr.mybrary.userservice.user.domain.UserServiceImplTest"

# 빌드 (테스트 스킵)
./gradlew clean build -x test

# QueryDSL Q-class 생성 포함 전체 컴파일
./gradlew compileJava

# QueryDSL 생성 소스 삭제
./gradlew clean   # src/main/generated 삭제됨

# API 문서 생성 (Spring REST Docs → OpenAPI 3 JSON)
./gradlew openapi3
```

---

## 공통 패키지 구조 (`user-service` / `book-service`)

```
kr.mybrary.<service>/
  <domain>/
    persistence/          # JPA Entity, Enum, repository/
      repository/         # JpaRepository + QueryDSL Custom 인터페이스/구현체
    domain/               # Service(인터페이스) + ServiceImpl, DTO, 도메인 예외
      dto/
        request/          # *ServiceRequest  (service layer 입력)
        response/         # *ServiceResponse (service layer 출력)
    presentation/         # Controller, presentation layer DTO
      dto/
        request/          # 요청 바디 (Bean Validation 적용)
        response/         # 응답 바디
  global/
    config/               # Spring 설정 클래스 (QueryDsl, Redis, Async 등)
    exception/            # ApplicationException, GlobalExceptionHandler
    aspect/               # RequestLoggingAspect (AOP 요청 로깅)
    dto/response/         # SuccessResponse, ErrorResponse (공통 응답 래퍼)
```

### 레이어 간 DTO 변환

- **MapStruct** 사용. `*DtoMapper` 인터페이스 + `@Mapper(componentModel = "spring")`.
- `presentation DTO → ServiceRequest` 변환은 Controller에서, `Entity → ServiceResponse` 변환은 Service에서 처리.
- `build.gradle`에서 annotation processor 순서가 중요: `lombok-mapstruct-binding` → `mapstruct-processor` → `lombok`.

### 예외 처리

모든 도메인 예외는 `ApplicationException`을 상속하며 `(status, errorCode, errorMessage)` 생성자를 사용한다. `GlobalExceptionHandler`가 `@RestControllerAdvice`로 캐치해 `ErrorResponse`로 응답한다.

```
ApplicationException(abstract)
  └─ BookNotFoundException(404, "BK-01", "...")
  └─ MyBookAccessDeniedException(403, "MBK-03", "...")
```

---

## Config 프로파일 구조

각 서비스의 `bootstrap.yml`은 Config Server URI와 active profile을 환경변수로 받는다.

```yaml
# bootstrap.yml
spring.cloud.config.uri: ${CONFIG_SERVER_URI}
spring.profiles.active: ${PROFILE}   # test | local | dev | prod
```

| 프로파일 | DB | Eureka | Config Server |
|---|---|---|---|
| `test` | H2 in-memory | disabled | disabled |
| `local` | MySQL localhost | disabled | disabled |
| `dev` / `prod` | AWS RDS MySQL | enabled | enabled |

로컬에서 테스트 실행 시 `bootstrap.yml`의 `spring.profiles.active`를 `test`로 설정하거나 환경변수 `PROFILE=test`를 주입하면 Config Server 없이 동작한다.

---

## 테스트 패턴

### 커스텀 어노테이션

- `@PersistenceTest` — `@DataJpaTest` + `@ActiveProfiles("test")` + `@AutoConfigureTestDatabase(replace=NONE)` 조합. 실제 H2 DB에 쿼리를 날린다.
- Repository 테스트는 `@PersistenceTest`를 사용한다.

### Fixture / TestData 클래스

- `*Fixture.java` — 테스트용 엔티티 빌더를 enum으로 정의 (`COMMON_USER`, `USER_WITH_FOLLOWER` 등).
- `*DtoTestData.java` — 테스트용 DTO 빌더 (서비스 request/response 객체 생성 헬퍼).

### Controller 테스트

`@WebMvcTest` + `MockMvc` + Spring REST Docs + `restdocs-api-spec` 조합.

```java
@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
```

테스트 실행 시 `build/generated-snippets/`에 REST Docs 스니펫이 생성되고, `./gradlew openapi3` 실행 시 `build/resources/main/static/docs/<service>.json`으로 합쳐진다.

---

## 주요 기술 상세

### API Gateway JWT 처리 (`apigateway-server`)

`AuthorizationHeaderFilter`가 모든 요청에 적용된다.
1. `Authorization` 헤더에서 access token 추출
2. Redis에서 로그아웃 토큰 블랙리스트 조회 (`redisUtil.hasKey(token)`)
3. JWT 서명/만료 검증
4. 검증 통과 시 `USER-ID` 헤더를 downstream 서비스에 주입

화이트리스트 경로(`/sign-up`, `/auth`, `/oauth2/authorization`)는 검증 생략.

### book-service 도서 검색

`PlatformBookSearchApiService` 인터페이스를 `KakaoBookSearchApiService`와 `AladinBookSearchApiService`가 구현한다. Resilience4j Retry를 적용해 외부 API 장애에 대응한다. 인기 검색어는 Redis Sorted Set으로 관리(`BookSearchRankingService`).

### Inter-service 통신

`book-service → user-service` 호출에 **Spring Cloud OpenFeign**을 사용한다 (`client/user/api/UserServiceClient.java`). 공통 Feign 응답 래퍼는 `FeignClientResponse`.

### Redis

- `user-service`: refresh token 저장, 로그아웃 access token 블랙리스트
- `book-service`: 도서 검색 캐시(`@Cacheable`), 인기 검색어 Sorted Set
- `apigateway-server`: 로그아웃 블랙리스트 조회
- 로컬/테스트: standalone Redis. 운영: ElastiCache Cluster (`RedisClusterConfig`).

### AWS S3 (user-service)

`StorageService` 인터페이스 — `AmazonS3Service`가 구현. 프로필 이미지 업로드 시 원본 + tiny/small 썸네일을 `profile/profileImage/{userId}/` 경로에 저장.

---

## CI/CD

`.github/workflows/`의 각 워크플로는 `master` 브랜치의 `backend/<service-name>/**` 경로 변경 시 트리거된다.

1. `./gradlew test`
2. `./gradlew clean build -x test`
3. Docker 이미지 빌드 → AWS ECR 푸시 (`--platform amd64`)
4. ECS Fargate 배포
