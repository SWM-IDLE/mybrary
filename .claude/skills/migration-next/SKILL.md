# migration-next 스킬

MIGRATION_PLAN.md의 체크리스트를 기반으로 다음 작업 브랜치를 생성한다.

---

## 브랜치 전략

- **Phase 단위**로 브랜치 1개 + PR 1개를 원칙으로 한다.
- 각 Phase의 모든 항목을 같은 브랜치에서 완료한 후 PR을 올린다.
- PR 머지 시 해당 Phase의 완료된 항목을 `- [x]`로 변경한 커밋을 포함한다.
- 브랜치명은 체크리스트 Phase 헤더의 `—` 뒤 백틱 안에 명시된다.

---

## 워크플로우

### 1단계: 열린 마이그레이션 PR 확인

```bash
gh pr list --base master-new --state open
```

`phase0/`, `phase1/`, `phase2/`, `phase3/` 로 시작하는 브랜치의 PR이 있으면:
- **중단하고 경고**: "진행 중인 PR이 있습니다. master-new에 머지한 후 다시 실행하세요."
- PR URL 안내

### 2단계: 최신 체크리스트 읽기

```bash
git fetch origin master-new
git show origin/master-new:MIGRATION_PLAN.md
```

`## 마이그레이션 체크리스트` 섹션에서 **첫 번째 `- [ ]` 항목이 포함된 Phase**를 찾는다.

Phase 헤더 형식:
```
### Phase N — [설명] — `branch-name`
```

백틱 안의 문자열을 브랜치명으로 추출한다.

체크리스트에 미완료 항목이 없으면:
- "모든 마이그레이션 항목이 완료되었습니다!" 출력 후 종료

### 3단계: 브랜치 생성 및 체크아웃

```bash
git checkout -b <branch-name> origin/master-new
```

### 4단계: 작업 안내 출력

```
다음 작업 브랜치가 생성되었습니다.

Phase: Phase 0 — 테스트 강화
브랜치: phase0/test-coverage (origin/master-new 기준)

미완료 항목:
- [ ] P0-02 user-service Redis 블랙리스트 + Refresh Token 통합 테스트
- [ ] P0-03 book-service UserServiceClient WireMock 계약 테스트 + Resilience4j Fallback
...

작업 완료 후:
1. MIGRATION_PLAN.md에서 완료 항목을 - [ ] → - [x] 로 변경하고 커밋
2. /pr-to-master-new 로 PR 생성
3. PR 머지 후 /migration-next 재실행 → 다음 Phase 브랜치 자동 생성
```

---

## 안전 규칙

1. **열린 마이그레이션 PR이 있으면 반드시 중단** — 병렬 작업 방지
2. **브랜치는 항상 `origin/master-new` 기준** — 로컬이 아닌 원격 기준
3. **미커밋 변경사항이 있으면 경고** — `git status` 확인 후 안내
4. **Phase 내 항목이 부분적으로 완료되었으면 해당 Phase 브랜치로 안내** — 새 브랜치 생성 안 함

---

## 예외 처리

| 상황 | 대응 |
|---|---|
| 열린 마이그레이션 PR 존재 | PR URL 안내 후 중단 |
| 현재 브랜치가 이미 해당 Phase 브랜치 | 추가 안내 없이 현재 브랜치 유지 |
| 브랜치명 충돌 | `git branch -a`로 확인 후 사용자에게 알림 |
| 체크리스트 항목 없음 | 마이그레이션 완료 메시지 출력 |
