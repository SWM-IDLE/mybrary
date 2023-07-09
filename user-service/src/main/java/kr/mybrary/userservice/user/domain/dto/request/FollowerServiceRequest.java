package kr.mybrary.userservice.user.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowerServiceRequest {

    private String targetId;
    private String sourceId;

    public static FollowerServiceRequest of(String loginId, String sourceId) {
        return FollowerServiceRequest.builder()
                .targetId(loginId)
                .sourceId(sourceId)
                .build();
    }

}
