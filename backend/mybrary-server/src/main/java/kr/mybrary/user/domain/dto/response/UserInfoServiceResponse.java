package kr.mybrary.user.domain.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoServiceResponse {

    List<UserInfoElement> userInfoElements;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoElement {

        private String userId;
        private String nickname;
        private String profileImageUrl;

    }

}
