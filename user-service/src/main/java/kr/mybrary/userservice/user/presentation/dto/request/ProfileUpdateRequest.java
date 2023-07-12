package kr.mybrary.userservice.user.presentation.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileUpdateRequest {

    private String nickname;
    private String email;
    private String introduction;

    @Builder
    public ProfileUpdateRequest(String nickname, String email, String introduction) {
        this.nickname = nickname;
        this.email = email;
        this.introduction = introduction;
    }
}
