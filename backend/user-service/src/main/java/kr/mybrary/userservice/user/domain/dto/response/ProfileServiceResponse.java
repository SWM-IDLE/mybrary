package kr.mybrary.userservice.user.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileServiceResponse {

    private String nickname;
    private String profileImageUrl;
    private String introduction;

}
