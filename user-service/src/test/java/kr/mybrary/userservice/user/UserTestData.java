package kr.mybrary.userservice.user;

import kr.mybrary.userservice.user.persistence.User;

public class UserTestData {

    public static User createUserWithProfile() {

        return User.builder()
                .id(1L)
                .loginId("loginId")
                .nickname("nickname")
                .email("email@mail.com")
                .introduction("introduction")
                .profileImageUrl("profileImageUrl")
                .build();
    }

}
