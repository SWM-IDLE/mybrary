package kr.mybrary.userservice.user.domain.dto.request;

import kr.mybrary.userservice.user.presentation.dto.request.ProfileUpdateRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileUpdateServiceRequest {

    private String loginId;
    private String nickname;
    private String email;
    private String introduction;

    public static ProfileUpdateServiceRequest of(ProfileUpdateRequest profileUpdateRequest, String loginId) {
        return ProfileUpdateServiceRequest.builder()
                .loginId(loginId)
                .nickname(profileUpdateRequest.getNickname())
                .email(profileUpdateRequest.getEmail())
                .introduction(profileUpdateRequest.getIntroduction())
                .build();
    }
}
