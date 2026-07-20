#!/bin/bash
# Bash 도구 전용 파일 접근 제어 훅
# 쉘 명령어를 통한 보호 파일 우회 시도를 차단

PAYLOAD=$(cat)
AGENT=$(echo "$PAYLOAD" | jq -r '.agent_name // ""')
CMD=$(echo "$PAYLOAD" | jq -r '.tool_input.command // ""')

reject() {
  local SUBJECT="${AGENT:-main}"
  echo "" >&2
  echo "🚫 [Bash 접근 거부] $1" >&2
  echo "   명령어   : $(echo "$CMD" | head -c 120)" >&2
  echo "   요청 주체: $SUBJECT" >&2
  echo "" >&2
  exit 2
}

# 쓰기 작업 여부 판단 (읽기 전용 명령은 통과)
is_write_op() {
  local file="$1"
  # git commit은 파일 쓰기 아님 — 커밋 메시지 본문 내 > 문자 오탐 방지
  echo "$CMD" | grep -q "^git commit" && return 1
  # 셸 리다이렉트/파일 조작 패턴만 탐지
  echo "$CMD" | grep -qE "(tee\s|sed\s+-i|awk.+>\s*\S|mv\s+\S+\s+.*$file|cp\s+\S+\s+.*$file|rm\s+.*$file|git\s+rm.*$file|truncate)" || \
  echo "$CMD" | grep -qP '(^|\s)>{1,2}\s*\S'
}

# ──────────────────────────────────────────────────────
# 1. SERVICE_POLICY.md → planner만 허용
# ──────────────────────────────────────────────────────
if echo "$CMD" | grep -q "SERVICE_POLICY.md"; then
  if is_write_op "SERVICE_POLICY.md"; then
    if [[ "$AGENT" != "planner" ]]; then
      reject "SERVICE_POLICY.md 쓰기는 planner 에이전트만 허용됩니다."
    fi
  fi
fi

# ──────────────────────────────────────────────────────
# 2. 백엔드 소스 → java-spring-backend / msa-to-monolithic만 허용
# ──────────────────────────────────────────────────────
if echo "$CMD" | grep -qE 'backend/.+/src/'; then
  if is_write_op "src"; then
    if [[ "$AGENT" != "java-spring-backend" && "$AGENT" != "msa-to-monolithic" ]]; then
      reject "백엔드 소스(backend/*/src/) 쓰기는 java-spring-backend 또는 msa-to-monolithic 에이전트만 허용됩니다."
    fi
  fi
fi

# ──────────────────────────────────────────────────────
# 3. 프론트엔드 소스 → flutter-webview-refactor만 허용 (서브에이전트 한정)
# ──────────────────────────────────────────────────────
if echo "$CMD" | grep -qE 'frontend/lib/'; then
  if is_write_op "lib"; then
    if [[ -n "$AGENT" && "$AGENT" != "flutter-webview-refactor" ]]; then
      reject "프론트엔드 소스(frontend/lib/) 쓰기는 flutter-webview-refactor 에이전트만 허용됩니다."
    fi
  fi
fi

# ──────────────────────────────────────────────────────
# 4. 에이전트/설정/스킬 → 메인 Claude만 허용 (서브에이전트 자기수정 방지)
# ──────────────────────────────────────────────────────
if echo "$CMD" | grep -qE '\.claude/(agents|settings|skills)/'; then
  if is_write_op ".claude"; then
    if [[ -n "$AGENT" ]]; then
      reject ".claude/ 내부 파일(agents/settings/skills) 쓰기는 메인 Claude만 허용됩니다."
    fi
  fi
fi

# ──────────────────────────────────────────────────────
# 5. CI/CD 워크플로우 → 메인 Claude만 허용
# ──────────────────────────────────────────────────────
if echo "$CMD" | grep -qE '\.github/workflows/'; then
  if is_write_op "workflows"; then
    if [[ -n "$AGENT" ]]; then
      reject ".github/workflows/ 쓰기는 메인 Claude만 허용됩니다."
    fi
  fi
fi

# ──────────────────────────────────────────────────────
# 6. MIGRATION_PLAN.md → msa-to-monolithic / 메인만 허용
# ──────────────────────────────────────────────────────
if echo "$CMD" | grep -q "MIGRATION_PLAN.md"; then
  if is_write_op "MIGRATION_PLAN.md"; then
    if [[ -n "$AGENT" && "$AGENT" != "msa-to-monolithic" ]]; then
      reject "MIGRATION_PLAN.md 쓰기는 msa-to-monolithic 에이전트 또는 메인 Claude만 허용됩니다."
    fi
  fi
fi

exit 0
