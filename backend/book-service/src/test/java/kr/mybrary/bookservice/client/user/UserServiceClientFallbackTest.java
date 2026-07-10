package kr.mybrary.bookservice.client.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kr.mybrary.bookservice.client.user.api.UserServiceClient;
import kr.mybrary.bookservice.client.user.dto.request.UserInfoRequest;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserServiceClientFallbackTest {

    private final UserServiceClient client = new UserServiceClient() {
        @Override
        public UserInfoServiceResponse getUsersInfo(UserInfoRequest userInfoRequest) {
            throw new UnsupportedOperationException();
        }
    };

    @Test
    @DisplayName("user-service 호출 실패 시 fallback은 userId로 임시 닉네임을 생성한다.")
    void fallback_returnsTemporaryNicknameFromUserId() {
        UserInfoRequest request = UserInfoRequest.of(List.of("abcde12345", "fghij67890"));

        UserInfoServiceResponse response = client.getUsersInfoFallback(request, new RuntimeException("timeout"));

        assertThat(response.getData().getUserInfoElements()).hasSize(2);
        assertThat(response.getData().getUserInfoElements().get(0).getNickname()).isEqualTo("user_abcde");
        assertThat(response.getData().getUserInfoElements().get(1).getNickname()).isEqualTo("user_fghij");
    }

    @Test
    @DisplayName("user-service 호출 실패 시 fallback은 기본 프로필 이미지 URL을 반환한다.")
    void fallback_returnsDefaultProfileImageUrl() {
        UserInfoRequest request = UserInfoRequest.of(List.of("user-1-id-here"));

        UserInfoServiceResponse response = client.getUsersInfoFallback(request, new RuntimeException("circuit open"));

        assertThat(response.getData().getUserInfoElements().get(0).getProfileImageUrl())
                .isEqualTo(UserServiceClient.DEFAULT_PROFILE_IMAGE_URL);
    }

    @Test
    @DisplayName("user-service 호출 실패 시 fallback은 요청한 userId 수만큼 원소를 반환한다.")
    void fallback_returnsSameCountAsRequestedUserIds() {
        List<String> userIds = List.of("a1b2c3d4e5", "f6g7h8i9j0", "k1l2m3n4o5");
        UserInfoRequest request = UserInfoRequest.of(userIds);

        UserInfoServiceResponse response = client.getUsersInfoFallback(request, new RuntimeException("server error"));

        assertThat(response.getData().getUserInfoElements()).hasSize(3);
    }
}
