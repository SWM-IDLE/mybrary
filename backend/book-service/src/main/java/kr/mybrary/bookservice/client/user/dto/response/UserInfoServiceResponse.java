package kr.mybrary.bookservice.client.user.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoServiceResponse {

    private List<UserInfoElement> userInfoElements;

    @Builder
    @Getter
    public static class UserInfoElement {
        private String userId;
        private String nickname;
        private String profileImageUrl;
    }
}
