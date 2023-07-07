package kr.mybrary.userservice.user.domain.dto.request;

import kr.mybrary.userservice.user.presentation.dto.request.FollowRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowServiceRequest {

    private String sourceId;
    private String targetId;

    public static FollowServiceRequest of(String loginId, FollowRequest followRequest) {
        return FollowServiceRequest.builder()
                .sourceId(loginId)
                .targetId(followRequest.getTargetId())
                .build();
    }
}
