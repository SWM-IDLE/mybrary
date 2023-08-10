package kr.mybrary.userservice.user.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IsFollowingServiceResponse {

    String requestLoginId;
    String targetLoginId;
    boolean isFollowing;

}
