package kr.mybrary.userservice.user.domain.dto.response;

import kr.mybrary.userservice.user.persistence.model.FollowUserInfoModel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowResponse {

    private String userId;
    private String nickname;
    private String profileImageUrl;

    public static FollowResponse of(FollowUserInfoModel followUserInfoModel) {
        return FollowResponse.builder()
                .userId(followUserInfoModel.getLoginId())
                .nickname(followUserInfoModel.getNickname())
                .profileImageUrl(followUserInfoModel.getProfileImageUrl())
                .build();
    }

}
