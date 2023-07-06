package kr.mybrary.userservice.user.domain.dto.response;

import kr.mybrary.userservice.user.persistence.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpServiceResponse {

    private String loginId;
    private String nickname;
    private String email;
    private Role role;

}
