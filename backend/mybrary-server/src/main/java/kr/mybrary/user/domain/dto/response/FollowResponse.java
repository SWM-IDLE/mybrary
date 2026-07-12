package kr.mybrary.user.domain.dto.response;

import kr.mybrary.user.persistence.model.FollowUserInfoModel;
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
