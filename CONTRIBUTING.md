# Contributing Guide

Mybrary 프로젝트에 기여하기 위한 가이드. 개발 환경 설정, 코드 컨벤션, 알려진 함정을 다룬다.

---

## 로컬 개발 환경

### 사전 요구사항

- Java 17
- Docker Desktop
- (선택) Flutter SDK — 프론트엔드 작업 시

### 백엔드 실행

```bash
# 1. DB + Redis 실행
cd backend/mybrary-server
docker compose up -d

# 2. 서버 실행 (포트 8090)
./gradlew bootRun --args='--spring.profiles.active=local'
```

- 접속: http://localhost:8090/web
- 어드민 계정: `admin` / `admin` (LocalDataInitializer가 자동 생성)
- 샘플 도서 10권 + MyBook 자동 시드됨

> **주의:** 8080 포트가 다른 프로세스에 점유될 수 있음. `application-local.yml`의 `server.port`가 `8090`으로 고정되어 있다.

### 소셜 로그인

로컬 환경에서 Kakao/Naver/Google 로그인은 동작하지 않는다. OAuth2 redirect URI가 `localhost:8090`으로 등록되지 않았기 때문. 운영 환경에서만 동작한다.

---

## 코드 컨벤션

### 커밋 메시지

[Conventional Commits](https://www.conventionalcommits.org/) 형식을 따른다.

```
<type>(<scope>): <subject>
```

| type | 사용 시점 |
|---|---|
| `feat` | 새 기능 |
| `fix` | 버그 수정 |
| `refactor` | 동작 변경 없는 코드 개선 |
| `test` | 테스트 추가/수정 |
| `chore` | 빌드, 의존성, 설정 |
| `docs` | 문서 |

### 브랜치 전략

| 브랜치 | 용도 |
|---|---|
| `master-new` | 메인 개발 브랜치 (PR 대상) |
| `phase<N>/...` | 마이그레이션 작업 단위 |
| `harness/...` | Claude Code 툴링 (스킬/에이전트/훅) |
| `feature/...` | 기능 개발 |

---

## 알려진 함정 (Gotchas)

프로젝트를 처음 접하거나 디버깅 중에 자주 만나는 함정. `/retrospect` 실행 시 팀 공유 가치가 있는 내용이 여기에 추가된다.

### Thymeleaf 3.1 — `#request` 비활성화

Thymeleaf 3.1부터 템플릿 내에서 `#request`, `#session`, `#response`가 기본 비활성화된다.

```
EL1007E: Property or field 'requestURI' cannot be found on null
```

**해결:** Controller의 `@ModelAttribute`로 필요한 값을 직접 주입한다.

```java
@ModelAttribute("currentPath")
public String currentPath(HttpServletRequest request) {
    return request.getRequestURI();
}
```

### 이중 SecurityFilterChain — 순서 중요

`/web/**` (세션 기반)과 `/api/v1/**` (JWT 기반) 두 FilterChain이 공존한다. `@Order` 를 잘못 설정하면 JWT 필터가 `/web/**` 요청을 가로챈다.

- `@Order(1)` → `/web/**` (세션 기반)
- `@Order(2)` → `/api/v1/**` (JWT 기반)

### LocalDataInitializer — MyBook 누락 함정

`initBooks()`가 `bookRepository.count() > 0` 조건으로 조기 반환한다. DB를 유지한 채 admin 계정만 새로 만들면 MyBook이 없는 상태가 된다.

`myBookRepository.countByUserId(adminId) == 0` 체크를 조기 반환 분기 안에도 추가해야 한다.

### GitGuardian 오탐 패턴

환경변수 기본값에 `password`, `secret`, `key` 키워드가 포함되면 GitGuardian이 탐지한다. 로컬 개발용 더미값은 커밋 전 GitGuardian 대시보드에서 false positive 처리한다.

```yaml
# 이런 패턴이 탐지됨 (실제 시크릿 아님)
MYSQL_ROOT_PASSWORD: mybrary
password: ${DB_PASSWORD:mybrary}
```

### `@Profile("local")` / `@Profile("!local")` 빈 전환

동일 인터페이스의 두 구현체(예: `LocalStorageService` vs `AmazonS3Service`)를 프로파일로 전환할 때, `!local` 표현식을 사용한다. `prod`만 명시하면 `dev` 프로파일에서 빈이 없어 애플리케이션이 뜨지 않는다.

---

## 마이그레이션 현황

`MIGRATION_PLAN.md` 참고. Phase 진행 상황과 다음 작업이 정리되어 있다.
