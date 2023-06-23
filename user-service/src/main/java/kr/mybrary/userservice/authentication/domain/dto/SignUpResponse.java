package kr.mybrary.userservice.authentication.domain.dto;

import kr.mybrary.userservice.user.persistence.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpResponse {

    private String loginId;

    private String password;

    private String nickname;

    private String email;

    private Role role;

}
