# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Spring Boot 3.1 / Java 17 모놀리식 백엔드. Phase 1 마이그레이션 완료로 단일 Gradle 프로젝트로 통합됨.

| 디렉토리 | 역할 |
|---|---|
| `mybrary-server` | 메인 모놀리식 서버 — user, book, mybook, review, interest 도메인 통합 |
| `user-service` | (구 MSA) 참조용으로만 유지, 빌드/배포 대상 아님 |
| `book-service` | (구 MSA) 참조용으로만 유지, 빌드/배포 대상 아님 |

> `apigateway-server`, `eureka-server`, `config-server`는 Phase 3에서 제거 완료.

---

## Build & Test Commands

```bash
cd backend/mybrary-server

# 테스트 실행
./gradlew test

# 빌드 (테스트 스킵)
./gradlew clean build -x test

# 로컬 실행 (Docker Compose로 MySQL + Redis 먼저 기동 필요)
docker compose up -d
./gradlew bootRun --args='--spring.profiles.active=local'
# → http://localhost:8090/web/login (admin / admin)

# 단일 테스트 클래스 실행
./gradlew test --tests "kr.mybrary.user.domain.UserServiceImplTest"

# API 문서 생성
./gradlew openapi3
```

---

## 패키지 구조

```
kr.mybrary/
  user/           # 회원가입·로그인, OAuth2, 팔로우, 프로필
  book/           # 도서 엔티티, 관심 도서
  booksearch/     # Kakao·Aladin 도서 검색 API
  mybook/         # MyBook CRUD, 독서 상태
  review/         # 리뷰 작성·수정·삭제
  interest/       # 관심사 카테고리, 추천 도서
  tag/            # 의미 태그 (MyBook 메모)
  authentication/ # JWT 로그인·로그아웃, OAuth2 핸들러
  web/            # Thymeleaf MPA WebController (/web/**)
  global/         # 공통 설정, JWT 필터, 예외 처리, BaseEntity
```

---

## 프로파일 구조

| 프로파일 | DB | Redis | 스토리지 | 용도 |
|---|---|---|---|---|
| `local` | MySQL (Docker) | Redis (Docker) | 로컬 파일 (`./local-uploads/`) | 로컬 개발 |
| `test` | H2 in-memory | localhost | — | 테스트 코드 |
| `prod` | AWS RDS MySQL | ElastiCache Cluster | AWS S3 | 운영 |

---

## 보안 구조

이중 SecurityFilterChain:
- `@Order(1)` `/web/**` — Spring Security 세션 기반 폼 로그인 (Thymeleaf MPA)
- `@Order(2)` 그 외 — JWT 기반 인증 (`JwtAuthenticationFilter`)

---

## CI/CD

`.github/workflows/mybrary-server-ecr-ecs.yml` — `master-new` 브랜치 푸시 시 트리거:
1. `./gradlew test` (test 프로파일)
2. `./gradlew clean build -x test`
3. Docker 이미지 빌드 → AWS ECR 푸시
4. ECS Fargate 배포
