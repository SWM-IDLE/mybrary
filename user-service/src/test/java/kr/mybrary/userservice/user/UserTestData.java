package kr.mybrary.userservice.user;

import kr.mybrary.userservice.user.domain.dto.request.ProfileUpdateServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;

public class UserTestData {

    public static SignUpServiceRequest createSignUpServiceRequest() {
        return SignUpServiceRequest.builder()
                .loginId("loginId")
                .password("password123!")
                .nickname("nickname")
                .email("email@mail.com")
                .build();
    }

    public static ProfileUpdateServiceRequest createProfileUpdateServiceRequest() {
        return ProfileUpdateServiceRequest.builder()
                .loginId("loginId")
                .nickname("updated_nickname")
                .email("updated_email@mail.com")
                .introduction("updated_introduction")
                .build();
    }
}
