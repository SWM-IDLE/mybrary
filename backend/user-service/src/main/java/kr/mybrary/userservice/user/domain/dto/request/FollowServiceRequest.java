package kr.mybrary.userservice.user.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowServiceRequest {

    private String sourceId;
    private String targetId;

    public static FollowServiceRequest of(String loginId, String targetId) {
        return FollowServiceRequest.builder()
                .sourceId(loginId)
                .targetId(targetId)
                .build();
    }
}
