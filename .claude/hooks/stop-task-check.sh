#!/bin/bash
# Stop 이벤트 훅 - 스킬/작업 완료 후 잔여 작업 확인 메시지 출력
# stop_hook_active == true 이면 훅이 이미 순환 중이므로 무시

PAYLOAD=$(cat)
STOP_HOOK_ACTIVE=$(echo "$PAYLOAD" | jq -r '.stop_hook_active // false')
STOP_REASON=$(echo "$PAYLOAD" | jq -r '.stop_reason // ""')

# 무한루프 방지: 훅이 주입한 응답에서 다시 훅이 돌면 조용히 종료
if [[ "$STOP_HOOK_ACTIVE" == "true" ]]; then
  exit 0
fi

# 정상 종료(end_turn)일 때만 메시지 출력
if [[ "$STOP_REASON" == "end_turn" ]]; then
  echo "📋 잔여 작업이 있으면 이어서 처리해주세요."
fi

exit 0
