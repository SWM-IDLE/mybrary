package kr.mybrary.userservice.user;

import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.userservice.user.persistence.User;

public class UserTestData {

    public static SignUpServiceRequest createSignUpServiceRequest() {
        return SignUpServiceRequest.builder()
                .loginId("loginId")
                .password("password123!")
                .nickname("nickname")
                .email("email@mail.com")
                .build();
    }
}
