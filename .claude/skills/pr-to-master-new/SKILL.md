---
name: pr-to-master-new
description: |
  현재 브랜치에서 master-new 브랜치로 PR을 생성하는 스킬.
  PR title과 description을 자동으로 작성하고 gh CLI로 PR을 생성한다.

  다음 상황에서 반드시 사용한다:
  - "PR 만들어줘", "PR 올려줘", "master-new에 PR 해줘"
  - "풀리퀘스트 생성해줘", "PR 생성해줘"
  - 작업 완료 후 master-new 브랜치로 머지 요청이 필요한 상황
---

# PR to master-new 스킬

현재 브랜치에서 `master-new`로 PR을 생성하는 워크플로우.

---

## 워크플로우

### 1단계: 현재 상태 파악

아래 명령을 병렬로 실행해서 PR에 포함될 내용을 파악한다.

```bash
git branch --show-current                        # 현재 브랜치 확인
git log --oneline master-new..HEAD               # master-new 이후 커밋 목록
git diff --stat master-new...HEAD                # 변경 파일 요약
git remote get-url origin                        # 원격 저장소 URL 확인
```

**확인할 것:**
- 현재 브랜치가 `master-new`가 아닌가? (같으면 중단)
- 원격에 푸시된 상태인가? 안 됐으면 먼저 push 후 진행
- 커밋이 1개 이상 있는가?

---

### 2단계: PR Title 작성

커밋 목록과 변경 파일을 분석해서 제목을 작성한다.

**Title 규칙:**
- 70자 이내
- 브랜치명 또는 주요 변경 내용을 기반으로 작성
- Conventional Commits 형식을 참고하되 PR 제목답게 자연스럽게 작성
- 한국어 가능

**Title 예시:**
```
[feat] 사용자 팔로우 기능 구현
[refactor] book-service OpenFeign → 내부 서비스 호출 전환
[chore] 프로젝트 전용 Claude 에이전트 3종 추가
```

---

### 3단계: PR Description 작성

아래 템플릿을 기반으로 실제 변경 내용에 맞게 채운다.

```markdown
## 개요
<!-- 이 PR이 무엇을 하는지 1~3줄로 요약 -->

## 변경 사항
<!-- 변경된 내용을 bullet point로 정리 -->
- 

## 관련 커밋
<!-- git log --oneline master-new..HEAD 결과 붙여넣기 -->

## 테스트
<!-- 테스트 방법 또는 확인 방법 -->
- [ ] 로컬 빌드 확인
- [ ] 테스트 코드 실행

## 참고 사항
<!-- 리뷰어가 알아야 할 특이사항, 주의점 등 (없으면 삭제) -->
```

---

### 4단계: 원격 브랜치 푸시 확인

```bash
git push origin <현재 브랜치명>
```

이미 푸시된 경우 스킵한다.

---

### 5단계: PR 생성

```bash
gh pr create \
  --base master-new \
  --head <현재 브랜치명> \
  --title "<작성된 title>" \
  --body "$(cat <<'EOF'
<작성된 description>

🤖 Generated with [Claude Code](https://claude.ai/claude-code)
EOF
)"
```

PR 생성 후 URL을 사용자에게 출력한다.

```bash
gh pr view --web   # 브라우저로 PR 열기 (선택)
```

---

## 안전 규칙

1. **base 브랜치는 항상 `master-new`** — 다른 브랜치로 PR을 보내지 않는다
2. **현재 브랜치가 `master-new`이면 중단** — 자기 자신에게 PR 불가
3. **푸시되지 않은 커밋이 있으면 먼저 push** — 원격에 없는 커밋은 PR에 포함되지 않음
4. **PR 생성 전 title/description을 사용자에게 보여주고 확인받는다**

---

## 예외 처리

| 상황 | 대응 |
|------|------|
| `master-new` 브랜치가 원격에 없음 | `gh pr create` 실패 — 원격에 `master-new` 존재 여부 먼저 확인 |
| 이미 PR이 존재함 | `gh pr list` 로 확인 후 기존 PR URL 안내 |
| `gh` CLI 미설치 | `brew install gh && gh auth login` 안내 |
| 커밋이 없음 | "master-new 대비 변경사항이 없습니다" 안내 후 중단 |
