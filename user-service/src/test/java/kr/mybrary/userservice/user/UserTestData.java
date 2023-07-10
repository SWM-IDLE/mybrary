package kr.mybrary.userservice.user;

import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.userservice.user.persistence.User;

public class UserTestData {

    public static User createUser() {
        return UserFixture.USER.getUser();
    }

    public static User createUserWithOutProfileImageUrl() {
        return UserFixture.USER_WITHOUT_PROFILE_IMAGE_URL.getUser();
    }

    public static SignUpServiceRequest createSignUpServiceRequest() {
        return SignUpServiceRequest.builder()
                .loginId("loginId")
                .password("password123!")
                .nickname("nickname")
                .email("email@mail.com")
                .build();
    }
}
