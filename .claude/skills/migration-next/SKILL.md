# migration-next 스킬

MIGRATION_PLAN.md의 체크리스트를 기반으로 다음 작업 브랜치를 생성한다.

---

## 동작 방식

- `origin/master-new`의 MIGRATION_PLAN.md를 **상태 원본**으로 사용한다.
- 각 `- [ ]` 항목 = 브랜치 1개 + PR 1개.
- PR이 master-new에 머지될 때, 해당 항목을 `- [x]`로 변경한 커밋이 포함되어야 한다.
- 다음 `/migration-next` 실행 시 master-new의 최신 체크리스트를 읽어 자동으로 다음 항목을 선택한다.

---

## 워크플로우

### 1단계: 열린 마이그레이션 PR 확인

```bash
gh pr list --base master-new --state open
```

- 마이그레이션 브랜치(phase0/, phase1/, phase2/, phase3/ 로 시작)에 해당하는 열린 PR이 있으면:
  - **중단하고 경고**: "진행 중인 PR이 있습니다. master-new에 머지한 후 다시 실행하세요."
  - PR URL 안내
- 없으면 다음 단계 진행

### 2단계: 최신 체크리스트 읽기

```bash
git fetch origin master-new
git show origin/master-new:MIGRATION_PLAN.md
```

`## 마이그레이션 체크리스트` 섹션에서 **첫 번째 `- [ ]`** 항목을 찾는다.

항목 형식:
```
- [ ] **P0-01** [설명] — `branch-name`
```

백틱(`` ` ``) 안의 문자열을 브랜치명으로 추출한다.

체크리스트에 미완료 항목이 없으면:
- "모든 마이그레이션 항목이 완료되었습니다! 🎉" 출력 후 종료

### 3단계: 브랜치 생성 및 체크아웃

```bash
git checkout -b <branch-name> origin/master-new
```

### 4단계: 작업 안내 출력

사용자에게 다음을 안내한다:

```
다음 작업 브랜치가 생성되었습니다.

항목: **P0-01** Testcontainers 환경 구성 (MySQL + Redis)
브랜치: phase0/testcontainers-setup (origin/master-new 기준)

작업 완료 후:
1. MIGRATION_PLAN.md에서 해당 항목을 - [ ] → - [x] 로 변경하고 커밋
2. /pr-to-master-new 로 PR 생성
3. PR 머지 후 /migration-next 재실행 → 다음 항목 자동 선택
```

---

## 안전 규칙

1. **열린 마이그레이션 PR이 있으면 반드시 중단** — 병렬 작업 방지
2. **브랜치는 항상 `origin/master-new` 기준** — 로컬 master-new가 아닌 원격 기준
3. **현재 브랜치에 미커밋 변경사항이 있으면 경고** — `git status` 확인 후 안내
4. **MIGRATION_PLAN.md 수정(`- [x]`)은 반드시 PR에 포함** — 머지 후 자동으로 진행 상황이 반영됨

---

## 예외 처리

| 상황 | 대응 |
|---|---|
| 열린 마이그레이션 PR 존재 | PR URL 안내 후 중단 |
| 미커밋 변경사항 존재 | 경고 후 계속 여부 확인 |
| 브랜치명 충돌 (이미 존재) | `git branch -a`로 확인 후 사용자에게 알림 |
| 체크리스트 항목 없음 | 마이그레이션 완료 메시지 출력 |
| `origin/master-new` fetch 실패 | 네트워크 오류 안내 |

---

## 예시 실행

```
$ /migration-next

[1/3] 열린 마이그레이션 PR 확인 중...
→ 없음. 진행합니다.

[2/3] origin/master-new 체크리스트 읽는 중...
→ 다음 항목: P0-01 - Testcontainers 환경 구성 (MySQL + Redis)
→ 브랜치명: phase0/testcontainers-setup

[3/3] 브랜치 생성 중...
→ 완료: phase0/testcontainers-setup (origin/master-new 기준)

작업 완료 후:
1. MIGRATION_PLAN.md 해당 항목 체크: - [ ] → - [x]
2. /pr-to-master-new 로 PR 생성
3. PR 머지 후 /migration-next 재실행
```
