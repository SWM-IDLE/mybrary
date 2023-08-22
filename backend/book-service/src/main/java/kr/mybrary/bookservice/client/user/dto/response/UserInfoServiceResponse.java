package kr.mybrary.bookservice.client.user.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoServiceResponse {

    private UserInfoList data;

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfoList {

        List<UserInfo> userInfoElements;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfo {

        private String userId;
        private String nickname;
        private String profileImageUrl;
    }
}
