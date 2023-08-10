package kr.mybrary.userservice.user.domain.dto.request;

import kr.mybrary.userservice.user.presentation.dto.request.ProfileUpdateRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileUpdateServiceRequest {

    private String userId;
    private String loginId;
    private String nickname;
    private String introduction;

    public static ProfileUpdateServiceRequest of(ProfileUpdateRequest profileUpdateRequest, String userId, String loginId) {
        return ProfileUpdateServiceRequest.builder()
                .userId(userId)
                .loginId(loginId)
                .nickname(profileUpdateRequest.getNickname())
                .introduction(profileUpdateRequest.getIntroduction())
                .build();
    }
}
