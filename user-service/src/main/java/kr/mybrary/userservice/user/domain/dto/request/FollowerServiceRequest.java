package kr.mybrary.userservice.user.domain.dto.request;

import kr.mybrary.userservice.user.presentation.dto.request.FollowerRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowerServiceRequest {

    private String targetId;
    private String sourceId;

    public static FollowerServiceRequest of(String loginId, FollowerRequest followerRequest) {
        return FollowerServiceRequest.builder()
                .targetId(loginId)
                .sourceId(followerRequest.getSourceId())
                .build();
    }

}
