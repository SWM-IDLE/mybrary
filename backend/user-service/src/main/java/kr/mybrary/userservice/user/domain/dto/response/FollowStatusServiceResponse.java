package kr.mybrary.userservice.user.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowStatusServiceResponse {

    String userId;
    String targetId;
    boolean isFollowing;

}
