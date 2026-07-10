# Mybrary 마이그레이션 계획

## 마이그레이션 체크리스트

> **Phase 단위로 브랜치 1개 + PR 1개**를 원칙으로 한다.  
> PR이 master-new에 머지되면 해당 Phase의 항목들을 `- [x]`로 변경 후 커밋에 포함한다.  
> `/migration-next` 스킬은 다음 미완료 Phase 브랜치를 자동 생성한다.

### Phase 0 — 테스트 강화 — `phase0/test-coverage`

- [x] **P0-01** Testcontainers 환경 구성 (MySQL + Redis)
- [x] **P0-02** user-service Redis 블랙리스트 + Refresh Token 통합 테스트
- [x] **P0-03** book-service UserServiceClient WireMock 계약 테스트 + Resilience4j Fallback
- [ ] **P0-04** user-service BookServiceClient WireMock 계약 테스트
- [ ] **P0-05** user-service 커버리지 80% 달성
- [ ] **P0-06** book-service 커버리지 80% 달성

### Phase 1 — 백엔드 모놀리식 전환 — `phase1/monolithic`

- [ ] **P1-01** mybrary-server 프로젝트 뼈대 생성 + Spring Cloud 의존성 제거
- [ ] **P1-02** user 도메인 패키지 이전
- [ ] **P1-03** book 도메인 패키지 이전
- [ ] **P1-04** OpenFeign → 포트 인터페이스 기반 내부 호출 전환 (순환 참조 방지)
- [ ] **P1-05** JWT Filter 통합 (apigateway AuthorizationHeaderFilter → Spring Security)
- [ ] **P1-06** Config Server 제거 + application.yml 설정 통합
- [ ] **P1-07** DB 스키마 통합 Flyway 마이그레이션 스크립트
- [ ] **P1-08** API 경로 재설계 (/user-service/, /book-service/ 프리픽스 제거)
- [ ] **P1-09** CI/CD 파이프라인 통합 (ECS/ECR 단일화)

### Phase 2 — 프론트엔드 WebView 전환 — `phase2/webview`

- [ ] **P2-01** Flutter webview_flutter 도입 + Shell 구조 변경
- [ ] **P2-02** JS Bridge 구현 (Flutter ↔ Web 양방향)
- [ ] **P2-03** 소셜 로그인 JS Bridge 연동 (Kakao/Naver/Google)
- [ ] **P2-04** API 경로 업데이트 + 웹 프론트엔드 API 설정
- [ ] **P2-05** FCM 푸시 알림 + 카메라·갤러리 네이티브 연동 검증

### Phase 3 — 정리 및 배포 — `phase3/cleanup`

- [ ] **P3-01** 구 MSA 서비스 코드 제거 (apigateway, eureka, config-server)
- [ ] **P3-02** CI/CD 파이프라인 정리 및 GitHub Actions 통합
- [ ] **P3-03** 스테이징 검증 + 운영 배포

---

## 목표

MSA(마이크로서비스 아키텍처) → 모놀리식 전환 + Flutter 앱 WebView 기반 전환.

**전환 전**
```
Flutter App
  → https://mybrary.kr (API Gateway)
      /user-service/** → user-service (JWT 발급, OAuth2, 팔로우, 관심사)
      /book-service/** → book-service (도서 검색, MyBook, 리뷰)
  Eureka (서비스 디스커버리)
  Config Server (GitHub private repo에서 설정 로드)
  Redis (토큰 블랙리스트 + Refresh Token 저장)

  [OpenFeign 양방향 호출]
  book-service → user-service: 리뷰 조회 시 사용자 닉네임·프로필 이미지 조회
  user-service → book-service: 관심사 기반 도서 추천 조회
```

**전환 후**
```
Flutter WebView Shell
  → 웹 프론트엔드 (React/Vue)
  → https://mybrary.kr/api/** (mybrary-server, 단일 Spring Boot)
  Redis (동일하게 유지)
```

---

## 현재 상태 분석

### 백엔드 구성 요소

| 서비스 | 소스 파일 | 테스트 파일 | 전환 후 |
|---|---|---|---|
| `user-service` | 119개 | 30개 | `mybrary-server`로 통합 |
| `book-service` | 135개 | 43개 | `mybrary-server`로 통합 |
| `apigateway-server` | — | — | **제거** (JWT 검증 → mybrary-server 내 Spring Security로) |
| `eureka-server` | — | — | **제거** |
| `config-server` | — | — | **제거** (GitHub config repo 설정 → application.yml로 통합) |

### API Gateway 라우팅 규칙

```
GET  /user-service/**  →  RewritePath 제거 후  user-service로 전달
GET  /book-service/**  →  RewritePath 제거 후  book-service로 전달
```

화이트리스트 (JWT 검증 생략): `/sign-up`, `/auth`, `/oauth2/authorization`

### JWT + Redis 구조

- **토큰 발급**: user-service의 `JwtUtil.java` (HMAC512, Access 1시간 / Refresh 2주)
- **토큰 검증**: apigateway-server의 `JwtUtil.java` (동일 secretKey로 검증만)
- **로그아웃 블랙리스트**: user-service `CustomLogoutHandler`가 Redis에 기록 → apigateway `AuthorizationHeaderFilter`가 읽어서 차단
- **Refresh Token**: Redis에 `loginId` 키로 저장 (user-service가 관리)
- **헤더 전파**: apigateway가 JWT에서 추출한 `loginId`를 `USER-ID` 헤더로 downstream에 주입

### OpenFeign 양방향 호출 (중요)

```
book-service
  └─ UserServiceClient → POST /api/v1/users/info
     · 리뷰 목록 조회 시 사용자 닉네임·프로필 이미지 일괄 조회
     · Resilience4j RetryConfig + CircuitBreakerConfig 적용
     · Fallback: 기본 프로필 이미지 URL 반환

user-service
  └─ BookServiceClient → GET /api/v1/books/recommendations/{type}/categories/{categoryId}
     · 관심사 기반 도서 추천 조회
```

> 두 서비스가 서로를 호출하므로 모놀리식 통합 시 **순환 참조**가 생기지 않도록 의존성 구조를 재설계해야 한다.

### 데이터베이스

| 서비스 | 로컬 스키마 | 프로덕션 |
|---|---|---|
| user-service | `USER_DB` | 환경변수 `${DB_URL}` |
| book-service | `mybrary` | 환경변수 `${DB_URL}` |

**별도 DB 사용** — 통합 시 스키마 병합 또는 단일 DB 내 다중 스키마 전략 결정 필요.

### Config Server

- Config Server가 GitHub private 저장소(`SWM-IDLE/config-repository`)를 바라봄
- 각 서비스 `bootstrap.yml`이 `${CONFIG_SERVER_URI}`로 설정 로드
- `test` 프로파일에서는 Config Server 없이 로컬 `application.yml` 직접 사용
- 통합 시: 모든 설정을 `mybrary-server/application.yml`로 직접 이관

### 프론트엔드 API 구조

- 베이스 URL: `https://mybrary.kr` (단일)
- 서비스 구분은 **경로 프리픽스**로: `/user-service/...`, `/book-service/...`
- JWT Refresh: `GET /user-service/auth/v1/refresh`
  - 응답 헤더 `Authorization`(새 Access Token) + `Authorization-Refresh`(새 Refresh Token) 추출
  - `FlutterSecureStorage`에 저장

### 현재 테스트 갭

| 영역 | 현황 | 위험도 |
|---|---|---|
| Redis 블랙리스트 (로그아웃) | 테스트 없음 | **높음** |
| Redis Refresh Token 저장/검증 | 테스트 없음 | **높음** |
| UserServiceClient (Feign) 실제 호출 | Mock만 존재, 계약 테스트 없음 | **높음** |
| BookServiceClient (Feign) 실제 호출 | Mock만 존재, 계약 테스트 없음 | **높음** |
| Resilience4j Fallback 동작 | 검증 없음 | **중간** |
| Token Refresh 전체 흐름 | 단위 테스트만 존재 | **중간** |
| Testcontainers | 미사용 (H2 in-memory만) | — |

---

## Phase 0: 백엔드 테스트 강화

> 리팩터링 중 회귀를 잡을 안전망. 전환 전에 완료해야 한다.

### 0-1. 현재 커버리지 측정

```bash
cd backend/user-service && ./gradlew test jacocoTestReport
cd backend/book-service && ./gradlew test jacocoTestReport
# build/reports/jacoco/test/html/index.html
```

### 0-2. Testcontainers 환경 구성

H2는 MySQL과 동작이 달라 실제 버그를 잡지 못하는 케이스가 있다. MySQL + Redis 컨테이너 기반 통합 테스트로 교체한다.

```gradle
// build.gradle (두 서비스 공통)
testImplementation 'org.testcontainers:junit-jupiter:1.19.3'
testImplementation 'org.testcontainers:mysql:1.19.3'
testImplementation 'com.redis.testcontainers:testcontainers-redis:1.6.4'
```

```java
// PersistenceTest.java를 Testcontainers 기반으로 교체
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public abstract class PersistenceTest {
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");
    // ...
}
```

### 0-3. Redis 테스트 신규 작성

**user-service**: `CustomLogoutHandlerTest.java`
- 로그아웃 호출 후 Access Token이 Redis 블랙리스트에 등록되는지
- 로그아웃 후 Refresh Token이 Redis에서 삭제되는지

**user-service**: `AuthenticationServiceImplTest.java` 보강
- Refresh Token 갱신 시 Redis 값이 교체되는지
- 만료된 Refresh Token으로 갱신 시도 시 예외 발생하는지

### 0-4. OpenFeign 계약 테스트 신규 작성

모놀리식 전환 시 Feign 인터페이스를 내부 서비스 호출로 교체하는데, 그 전에 현재 계약(요청/응답 구조)을 테스트로 고정해둔다.

**book-service**: `UserServiceClientContractTest.java`
- `getUsersInfo()` 요청 바디 구조 검증
- Fallback 응답(기본 프로필 이미지 URL)이 정상 반환되는지 검증

**user-service**: `BookServiceClientContractTest.java`
- `getBookListByCategoryId()` 경로 파라미터 / 응답 구조 검증

WireMock을 사용해 실제 HTTP 호출을 stub한다:

```gradle
testImplementation 'org.wiremock:wiremock-standalone:3.3.1'
```

### 0-5. Resilience4j Fallback 테스트

```java
// CircuitBreaker가 열렸을 때 기본 프로필 이미지 URL을 반환하는지 검증
@Test
void getUsersInfo_fallback_whenCircuitBreakerOpen() { ... }
```

### 0-6. 완료 기준

- [ ] user-service 라인 커버리지 80% 이상
- [ ] book-service 라인 커버리지 80% 이상
- [ ] Redis 블랙리스트 테스트 통과 (Testcontainers Redis)
- [ ] Refresh Token 흐름 통합 테스트 통과
- [ ] Feign 계약 테스트 통과 (WireMock)

---

## Phase 1: 백엔드 모놀리식 전환

### 1-1. 새 프로젝트 구조

```
backend/mybrary-server/
  src/main/java/kr/mybrary/
    user/            ← user-service 이전 (패키지 kr.mybrary.userservice → kr.mybrary.user)
    book/            ← book-service 이전 (패키지 kr.mybrary.bookservice → kr.mybrary.book)
    global/
      jwt/           ← JwtUtil 통합 (user-service + apigateway 양쪽 JwtUtil → 하나로)
      security/      ← Spring Security 설정 (apigateway AuthorizationHeaderFilter 역할 흡수)
      redis/         ← RedisUtil 통합
      exception/     ← 공통 예외 처리
  src/main/resources/
    application.yml           ← config-server에서 관리하던 설정 직접 기재
    application-local.yml
    application-prod.yml
```

### 1-2. 의존성 재구성

**제거**
```gradle
// 제거 대상
'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
'org.springframework.cloud:spring-cloud-starter-openfeign'
'org.springframework.cloud:spring-cloud-config-client'
'org.springframework.cloud:spring-cloud-starter-bootstrap'
```

**유지 / 통합**
```gradle
// 유지
'org.springframework.boot:spring-boot-starter-security'  // JWT + Security
'org.springframework.boot:spring-boot-starter-data-redis'
'org.springframework.boot:spring-boot-starter-data-jpa'
'com.querydsl:querydsl-jpa'
'io.github.resilience4j:resilience4j-spring-boot3'  // 외부 API(Kakao/Aladin)에 한정 유지
'com.auth0:java-jwt:4.2.1'
```

### 1-3. 순환 참조 해결 (OpenFeign 제거)

두 서비스가 서로를 Feign으로 호출하므로, 단순히 서비스 클래스를 주입하면 순환 참조가 발생한다.

**해결 전략: 인터페이스 분리(Interface Segregation)**

```
book.domain.review
  └─ UserProfilePort (interface)   ← book 패키지 내 정의

user.domain.user
  └─ UserProfileAdapter            ← UserProfilePort 구현체 (user 패키지)

book.domain.book
  └─ BookRecommendationPort (interface)  ← user 패키지 내 정의

book.domain.book
  └─ BookRecommendationAdapter     ← BookRecommendationPort 구현체 (book 패키지)
```

```java
// book 패키지가 user 패키지에 직접 의존하는 대신 인터페이스에 의존
// book/domain/review/port/UserProfilePort.java
public interface UserProfilePort {
    List<UserInfoResponse> getUsersInfo(List<String> loginIds);
}

// user/domain/user/adapter/UserProfileAdapter.java
@Component
public class UserProfileAdapter implements UserProfilePort {
    private final UserService userService;
    // UserService를 직접 호출
}
```

### 1-4. JWT / Security 통합

apigateway가 담당하던 JWT 검증을 Spring Security Filter로 이관한다.

```java
// global/jwt/JwtAuthenticationFilter.java
// 기존 AuthorizationHeaderFilter 로직을 OncePerRequestFilter로 구현
// - Authorization 헤더 추출
// - Redis 블랙리스트 확인
// - JWT 검증 후 SecurityContext에 Authentication 설정
```

**USER-ID 헤더 → SecurityContext 전환**

기존에 downstream 서비스가 `USER-ID` 헤더에서 loginId를 읽던 방식을 `SecurityContextHolder`로 교체한다.

```java
// 전환 전 (user-service Controller)
@GetMapping("/profile")
public ResponseEntity<?> getProfile(@RequestHeader("USER-ID") String loginId) { ... }

// 전환 후
@GetMapping("/profile")
public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) { ... }
```

### 1-5. 설정 통합 (Config Server 제거)

config-server가 GitHub 저장소에서 로드하던 설정을 application.yml에 직접 기재한다.

```yaml
# application-prod.yml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  data:
    redis:
      host: ${REDIS_HOST}
      port: 6379
  security:
    oauth2:
      client:
        registration:
          kakao:
            client-id: ${KAKAO_CLIENT_ID}
          naver:
            client-id: ${NAVER_CLIENT_ID}
          google:
            client-id: ${GOOGLE_CLIENT_ID}

jwt:
  secret: ${JWT_SECRET}
  access-expiration: 3600000
  refresh-expiration: 1209600000

external:
  kakao-book-api:
    key: ${KAKAO_BOOK_API_KEY}
  aladin-api:
    ttb-key: ${ALADIN_TTB_KEY}
```

### 1-6. 데이터베이스 통합

두 서비스가 별도 스키마(`USER_DB`, `mybrary`)를 사용하므로 통합 전략을 결정한다.

**옵션 A (권장): 단일 스키마로 병합**
```sql
-- mybrary 스키마로 통합
-- user-service 테이블을 mybrary로 이관
-- JPA 엔티티의 @Table(schema = ...) 없애거나 단일화
```

**옵션 B: 단일 DB 내 다중 스키마 유지**
```yaml
# user 도메인
spring.datasource.url: jdbc:mysql://host:3306/USER_DB
# book 도메인
spring.datasource.url: jdbc:mysql://host:3306/mybrary
# → DataSource를 두 개로 분리하는 복잡도가 생기므로 비권장
```

> 옵션 A로 진행 시 DB 마이그레이션 스크립트(Flyway) 작성 필요.

### 1-7. API 경로 재설계

apigateway의 RewritePath로 제거되던 `/user-service/`, `/book-service/` 프리픽스가 이제 없어진다.  
하위 호환성을 위해 두 가지 방법 중 선택:

**옵션 A: 기존 경로 유지 (프론트 변경 최소화)**
```java
// 컨트롤러에 기존 경로 그대로 매핑
@RequestMapping("/api/v1/users")    // user-service 기존과 동일
@RequestMapping("/api/v1/books")    // book-service 기존과 동일
// 단, /user-service/ 프리픽스는 프론트에서 제거 필요
```

**옵션 B: /api/** 로 통일 (권장)**
```java
@RequestMapping("/api/v1/users")
@RequestMapping("/api/v1/books")
// 프론트엔드도 함께 정리
```

### 1-8. 완료 기준

- [ ] Phase 0 테스트 전체 통과
- [ ] Eureka, API Gateway, Config Server 없이 단독 기동
- [ ] user-service 전체 API 동작 확인
- [ ] book-service 전체 API 동작 확인
- [ ] 순환 참조 없이 리뷰 조회 시 사용자 정보 정상 반환
- [ ] 관심사 기반 도서 추천 정상 동작
- [ ] Redis 로그아웃 블랙리스트 정상 동작
- [ ] Kakao / Aladin 외부 API 호출 정상
- [ ] Resilience4j Fallback 동작 확인

---

## Phase 2: 프론트엔드 WebView 전환

> Flutter 앱은 네이티브 기능을 위한 Shell로 유지하고, 실제 UI는 웹으로 제공한다.

### 2-1. Flutter 구조 변경

```
lib/
  main.dart              ← 최소화 (WebView 로드)
  webview/
    webview_screen.dart  ← 앱의 메인 화면
    js_bridge.dart       ← Flutter ↔ Web 양방향 메시지 채널
  native/
    auth/
      social_login.dart  ← Kakao/Naver/Google SDK 네이티브 처리
    notification/
      push_notification.dart  ← FCM 유지
    media/
      camera_handler.dart     ← 카메라/갤러리 접근
```

```yaml
# pubspec.yaml 추가
dependencies:
  webview_flutter: ^4.x
  webview_flutter_android: ^3.x
  webview_flutter_wkwebview: ^3.x
```

### 2-2. API 경로 변경

apigateway의 RewritePath로 투명하게 제거되던 경로 프리픽스가 없어지므로 URL을 업데이트한다.

```dart
// frontend/lib/data/network/api.dart

// 전환 전 (apigateway가 /user-service/ 프리픽스를 제거해줬음)
static const String tokenRefresh = '/user-service/auth/v1/refresh';
static const String signUp       = '/user-service/api/v1/users/sign-up';
static const String bookSearch   = '/book-service/api/v1/books/search';

// 전환 후 (프리픽스 제거)
static const String tokenRefresh = '/auth/v1/refresh';
static const String signUp       = '/api/v1/users/sign-up';
static const String bookSearch   = '/api/v1/books/search';
```

> WebView 기반으로 전환하면 API 호출은 웹 프론트엔드(JS)가 직접 하므로, 기존 Flutter Dio 클라이언트는 점진적으로 제거된다.

### 2-3. JS Bridge 설계

**Flutter → Web (토큰·사용자 정보 전달)**
```dart
// 앱 초기화 시 웹에 토큰 주입
webViewController.runJavaScript(
  'window.mybraryBridge.init(${jsonEncode({"accessToken": token})})'
);
```

**Web → Flutter (네이티브 기능 요청)**
```dart
webViewController.addJavaScriptChannel(
  'FlutterBridge',
  onMessageReceived: (msg) {
    final data = jsonDecode(msg.message);
    switch (data['action']) {
      case 'socialLogin': handleSocialLogin(data['provider']); break;
      case 'openCamera':  handleCamera(); break;
    }
  },
);
```

### 2-4. 네이티브 기능 처리 목록

| 기능 | 현재 (Flutter) | 전환 후 처리 |
|---|---|---|
| FCM 푸시 알림 | Flutter 네이티브 | Flutter 네이티브 유지 |
| Kakao/Naver/Google 소셜 로그인 | Flutter SDK | Flutter SDK → JS Bridge로 결과 전달 |
| 카메라·갤러리 (프로필 이미지) | Flutter | JS Bridge → Flutter |
| 딥링크 | Flutter | Flutter 네이티브 유지, WebView URL로 전달 |
| JWT 토큰 저장 (`FlutterSecureStorage`) | Flutter | Flutter 유지 (웹에서 직접 접근 불가) |
| 앱 버전 체크 / 강제 업데이트 | Flutter | Flutter 네이티브 유지 |

### 2-5. 완료 기준

- [ ] WebView에서 웹 프론트엔드 정상 로드
- [ ] JWT 발급·갱신 흐름 정상 동작
- [ ] 소셜 로그인 JS Bridge 정상 동작 (Kakao/Naver/Google)
- [ ] FCM 푸시 알림 수신 정상
- [ ] 카메라·갤러리 JS Bridge 정상 동작
- [ ] Android / iOS 모두 검증

---

## Phase 3: 정리 및 배포

### 3-1. 제거 대상

- [ ] `backend/apigateway-server/`
- [ ] `backend/eureka-server/`
- [ ] `backend/config-server/`
- [ ] GitHub config 저장소 (`SWM-IDLE/config-repository`) 접근 불필요 확인
- [ ] `.github/workflows/` 에서 제거된 서비스 CI 파이프라인 삭제

### 3-2. 인프라 변경

| 항목 | 전환 전 | 전환 후 |
|---|---|---|
| ECR 이미지 | user-service, book-service, gateway, eureka, config | mybrary-server |
| ECS Task | 5개 | 1개 |
| 환경변수 관리 | Config Server → GitHub 저장소 | ECS Task Definition 직접 환경변수 주입 또는 AWS Secrets Manager |

### 3-3. 배포 체크리스트

- [ ] 스테이징 환경 전체 기능 회귀 테스트
- [ ] DB 마이그레이션 롤백 스크립트 준비
- [ ] Blue/Green 또는 순차 배포로 트래픽 전환
- [ ] 이전 MSA 서비스 ECS Task 종료
- [ ] Config 저장소 접근 환경변수 제거 확인

---

## 작업 순서 요약

```
Phase 0 — 테스트 강화 (전환 전 안전망 확보)
  0-1. 현재 커버리지 측정
  0-2. Testcontainers 환경 구성 (MySQL + Redis)
  0-3. Redis 블랙리스트 / Refresh Token 테스트 신규 작성
  0-4. Feign 계약 테스트 신규 작성 (WireMock)
  0-5. Resilience4j Fallback 테스트
  0-6. 커버리지 목표 달성 확인

Phase 1 — 백엔드 모놀리식 전환
  1-1. mybrary-server 프로젝트 뼈대 생성
  1-2. 의존성 통합 (Spring Cloud 제거)
  1-3. OpenFeign → 포트 인터페이스 기반 내부 호출 (순환 참조 방지)
  1-4. JWT Filter 통합 (apigateway 역할 흡수)
  1-5. Config Server 설정 → application.yml 이관
  1-6. DB 스키마 통합 (Flyway 마이그레이션)
  1-7. API 경로 재설계 (/user-service/, /book-service/ 프리픽스 제거)
  1-8. 전체 테스트 통과 확인

Phase 2 — 프론트엔드 WebView 전환
  2-1. Flutter Shell 구조 변경 + webview_flutter 도입
  2-2. JS Bridge 설계 및 구현
  2-3. 웹 프론트엔드 개발
  2-4. API 경로 업데이트 (프리픽스 제거)
  2-5. 네이티브 기능 연동 검증 (소셜 로그인, 푸시, 카메라)

Phase 3 — 정리 및 배포
  3-1. 구 MSA 서비스 코드 및 CI 파이프라인 제거
  3-2. ECS / ECR 인프라 통합
  3-3. 운영 배포 및 검증
```

---

## 주요 리스크

| 리스크 | 내용 | 완화 방법 |
|---|---|---|
| 순환 참조 | book ↔ user 양방향 Feign 호출 | Phase 1-3의 포트 인터페이스 패턴으로 의존성 단방향화 |
| DB 마이그레이션 | 두 스키마 병합 시 데이터 유실 | Flyway 스크립트 + 롤백 계획 + 스테이징 검증 |
| Config 유실 | GitHub config repo 설정 누락 | 모든 환경변수 목록화 후 ECS Task Definition에 이관 |
| Redis 블랙리스트 단절 | apigateway가 읽던 Redis를 mybrary-server가 직접 읽게 됨 | Phase 0에서 통합 테스트로 검증 |
| 프론트 경로 불일치 | /user-service/ 프리픽스 제거로 인한 404 | Phase 1-7과 Phase 2-4를 함께 진행 |
