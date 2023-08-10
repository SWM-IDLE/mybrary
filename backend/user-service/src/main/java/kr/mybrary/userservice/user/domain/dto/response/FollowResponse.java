package kr.mybrary.userservice.user.domain.dto.response;

import kr.mybrary.userservice.user.persistence.model.UserInfoModel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowResponse {

    private String userId;
    private String nickname;
    private String profileImageUrl;

    public static FollowResponse of(UserInfoModel userInfoModel) {
        return FollowResponse.builder()
                .userId(userInfoModel.getLoginId())
                .nickname(userInfoModel.getNickname())
                .profileImageUrl(userInfoModel.getProfileImageUrl())
                .build();
    }

}
