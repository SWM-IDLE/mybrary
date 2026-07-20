#!/bin/bash
# Edit / Write 도구 전용 파일 접근 제어 훅
# 보호 규칙에 위반되면 exit 2 (reject)

PAYLOAD=$(cat)
AGENT=$(echo "$PAYLOAD" | jq -r '.agent_name // ""')
FILE_PATH=$(echo "$PAYLOAD" | jq -r '.tool_input.file_path // ""')

REPO_ROOT=$(git rev-parse --show-toplevel 2>/dev/null || echo "/Users/kangminseong/repository/mybrary")

# 절대경로를 상대경로로 변환
if [[ "$FILE_PATH" == "$REPO_ROOT"/* ]]; then
  REL="${FILE_PATH#"$REPO_ROOT"/}"
else
  REL="$FILE_PATH"
fi

reject() {
  local SUBJECT="${AGENT:-main}"
  echo "" >&2
  echo "🚫 [파일 접근 거부] $1" >&2
  echo "   파일    : $REL" >&2
  echo "   요청 주체: $SUBJECT" >&2
  echo "" >&2
  exit 2
}

# ──────────────────────────────────────────────────────
# 1. SERVICE_POLICY.md → planner 에이전트만 허용
# ──────────────────────────────────────────────────────
if [[ "$REL" == "SERVICE_POLICY.md" ]]; then
  if [[ "$AGENT" != "planner" ]]; then
    reject "SERVICE_POLICY.md 는 planner 에이전트만 수정할 수 있습니다."
  fi
fi

# ──────────────────────────────────────────────────────
# 2. 백엔드 소스 → java-spring-backend / msa-to-monolithic만 허용
# ──────────────────────────────────────────────────────
if echo "$REL" | grep -qE '^backend/.+/src/'; then
  if [[ "$AGENT" != "java-spring-backend" && "$AGENT" != "msa-to-monolithic" ]]; then
    reject "백엔드 소스(backend/*/src/)는 java-spring-backend 또는 msa-to-monolithic 에이전트만 수정할 수 있습니다."
  fi
fi

# ──────────────────────────────────────────────────────
# 3. 프론트엔드 소스 → flutter-webview-refactor만 허용 (서브에이전트 한정)
# ──────────────────────────────────────────────────────
if echo "$REL" | grep -qE '^frontend/lib/'; then
  if [[ -n "$AGENT" && "$AGENT" != "flutter-webview-refactor" ]]; then
    reject "프론트엔드 소스(frontend/lib/)는 flutter-webview-refactor 에이전트만 수정할 수 있습니다."
  fi
fi

# ──────────────────────────────────────────────────────
# 4. 에이전트 정의 파일 → 메인 Claude만 허용 (서브에이전트 자기수정 방지)
# ──────────────────────────────────────────────────────
if echo "$REL" | grep -qE '^\.claude/agents/'; then
  if [[ -n "$AGENT" ]]; then
    reject ".claude/agents/ 파일은 메인 Claude만 수정할 수 있습니다. 에이전트가 자신의 정의를 수정할 수 없습니다."
  fi
fi

# ──────────────────────────────────────────────────────
# 5. Claude 설정 파일 → 메인 Claude만 허용
# ──────────────────────────────────────────────────────
if echo "$REL" | grep -qE '^\.claude/settings'; then
  if [[ -n "$AGENT" ]]; then
    reject ".claude/settings*.json 은 메인 Claude만 수정할 수 있습니다."
  fi
fi

# ──────────────────────────────────────────────────────
# 6. 스킬 파일 → 메인 Claude만 허용
# ──────────────────────────────────────────────────────
if echo "$REL" | grep -qE '^\.claude/skills/'; then
  if [[ -n "$AGENT" ]]; then
    reject ".claude/skills/ 는 메인 Claude만 수정할 수 있습니다."
  fi
fi

# ──────────────────────────────────────────────────────
# 7. CI/CD 워크플로우 → 메인 Claude만 허용
# ──────────────────────────────────────────────────────
if echo "$REL" | grep -qE '^\.github/workflows/'; then
  if [[ -n "$AGENT" ]]; then
    reject ".github/workflows/ 는 메인 Claude만 수정할 수 있습니다. CI/CD 파이프라인은 보호됩니다."
  fi
fi

# ──────────────────────────────────────────────────────
# 8. 마이그레이션 계획서 → msa-to-monolithic / 메인만 허용
# ──────────────────────────────────────────────────────
if [[ "$REL" == "MIGRATION_PLAN.md" ]]; then
  if [[ -n "$AGENT" && "$AGENT" != "msa-to-monolithic" ]]; then
    reject "MIGRATION_PLAN.md 는 msa-to-monolithic 에이전트 또는 메인 Claude만 수정할 수 있습니다."
  fi
fi

exit 0
