package kr.mybrary.userservice.user.domain.dto;

import kr.mybrary.userservice.user.persistence.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponse {

    private String nickname;
    private String profileImageUrl;
    private String email;
    private String introduction;

}
