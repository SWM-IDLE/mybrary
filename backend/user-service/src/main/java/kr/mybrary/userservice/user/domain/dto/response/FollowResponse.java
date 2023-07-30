package kr.mybrary.userservice.user.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowResponse {

    private Long id;
    private String loginId;
    private String nickname;
    private String profileImageUrl;

}
