#!/bin/bash
# Stop 이벤트 훅 - 세션 종료 시 /retrospect 자동 실행
# stop_hook_active == true 이면 훅이 이미 순환 중이므로 무시

PAYLOAD=$(cat)
STOP_HOOK_ACTIVE=$(echo "$PAYLOAD" | jq -r '.stop_hook_active // false')
STOP_REASON=$(echo "$PAYLOAD" | jq -r '.stop_reason // ""')

# 무한루프 방지: 훅이 주입한 응답에서 다시 훅이 돌면 조용히 종료
if [[ "$STOP_HOOK_ACTIVE" == "true" ]]; then
  exit 0
fi

# /retrospect 는 사용자가 직접 호출

exit 0
