package kr.mybrary.userservice.user.domain.dto.response;

import kr.mybrary.userservice.user.persistence.model.UserInfoModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserInfoServiceResponse {

    List<UserInfoElement> userInfoElements;

    @Getter
    @Builder
    public static class UserInfoElement {

        private String userId;
        private String nickname;
        private String profileImageUrl;

    }

}
