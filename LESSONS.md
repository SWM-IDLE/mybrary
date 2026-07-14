# LESSONS.md

이 프로젝트에서 세션별로 배운 내용 누적. `/retrospect` 스킬이 유지 관리한다.

---

## 2026-07-14

### 작업 내용
- Thymeleaf MPA 14개 화면 구현 (login/signup/home/search/mybook/profile/interests/settings 등)
- 로컬 개발 환경 구성: Docker Compose (MySQL 8.0 + Redis 7.2) + 파일 기반 스토리지
- `LocalDataInitializer`: admin/admin 계정 + 샘플 도서 10권 + MyBook 자동 시드
- 이중 SecurityFilterChain: `/web/**` 세션 기반, `/api/v1/**` JWT 기반
- Phase 3: apigateway-server / eureka-server / config-server 디렉토리 삭제, 구 CI/CD 워크플로 삭제
- `harness/tooling` 브랜치 생성 + `/retrospect` 스킬 구현

### 이슈 & 해결

| 이슈 | 원인 | 해결 |
|------|------|------|
| Thymeleaf `#request` 가 null → `EL1007E: Property or field 'requestURI' cannot be found on null` | Thymeleaf 3.1부터 `#request`, `#session`, `#response` 기본 비활성화 | `WebController`에 `@ModelAttribute("currentPath") HttpServletRequest` 추가 → `${currentPath}` 로 참조 |
| 로컬 서버 8080 포트 충돌 | 다른 프로세스(habit-tracker)가 8080 점유 | `application-local.yml`에 `server.port: 8090` 추가 |
| admin 계정 교체 후 MyBook 없음 | `initBooks()`가 `bookRepository.count() > 0` 조건으로 조기 return → 신규 admin은 MyBook 0개 | admin 계정 조기 반환 분기 안에 `myBookRepository.countByUserId(ADMIN_LOGIN_ID) == 0` 체크 추가해 MyBook 재등록 |
| GitGuardian 오탐 2건 | `MYSQL_ROOT_PASSWORD: mybrary` (docker-compose.yml), `password: ${DB_PASSWORD:mybrary}` (application-local.yml) 키워드 패턴 탐지 | 실제 시크릿 아님. GitGuardian 대시보드에서 "Mark as false positive" 처리 |
| `my_book_meaning_tag` FK로 `my_book` 삭제 불가 | FK 제약 | `SET FOREIGN_KEY_CHECKS=0` 후 cascade delete |

### 다음 세션 체크리스트
- [ ] PR #91 (`phase3/cleanup`) 머지 확인
- [ ] P3-03 스테이징 검증 + 운영 배포 (AWS 접근 필요 — 사용자가 직접 처리)
- [ ] Aladin API key 세팅 (사용자가 직접)
- [ ] 소셜 로그인(Kakao/Naver/Google) 로컬 미동작 — OAuth2 redirect URI가 localhost:8090으로 등록되지 않음. 운영 환경에서만 동작
- [ ] `harness/tooling` PR to master-new

### 기억할 패턴
- **Thymeleaf 3.1 보안 제한**: `#request`, `#session`, `#response`는 기본 비활성화. 서블릿 객체가 필요하면 `@ModelAttribute`로 Controller에서 주입할 것
- **이중 SecurityFilterChain 순서**: `@Order(1)` = 웹(세션), `@Order(2)` = API(JWT). `securityMatcher`로 경로 분리. 순서 틀리면 JWT 필터가 `/web/**`를 잡아버림
- **`@Profile("local")` + `@Profile("!local")`**: 동일 인터페이스 두 구현체 중 로컬/운영 선택. `StorageService` 패턴으로 S3 ↔ 파일 시스템 전환
- **GitGuardian 오탐 패턴**: 환경변수 기본값에 `password`, `secret`, `key` 같은 키워드가 들어가면 탐지됨. 로컬 전용 더미값은 커밋 전 GitGuardian 대시보드에서 false positive 등록
- **MyBook 시드 로직 함정**: `bookRepository.count() > 0` 으로 중복 방지 시, 사용자(admin) 교체나 DB 리셋 후 MyBook이 빠질 수 있음. 사용자별 카운트 체크를 별도로 해야 함

---
