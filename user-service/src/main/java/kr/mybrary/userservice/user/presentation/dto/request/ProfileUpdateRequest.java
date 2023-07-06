package kr.mybrary.userservice.user.presentation.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileUpdateRequest {

    private String nickname;
    private String email;
    private String introduction;

}
