package kr.mybrary.userservice.user;

import java.util.Collections;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.SocialType;
import kr.mybrary.userservice.user.persistence.User;

public class UserTestData {

    public static User createUser() {

            return User.builder()
                    .id(1L)
                    .loginId("loginId")
                    .nickname("nickname")
                    .password("password")
                    .role(Role.USER)
                    .socialId("socialId")
                    .socialType(SocialType.GOOGLE)
                    .refreshToken("refreshToken")
                    .email("email@mail.com")
                    .introduction("introduction")
                    .profileImageUrl("profileImageUrl")
                    .followers(Collections.emptyList())
                    .followings(Collections.emptyList())
                    .build();
    }

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

    public static User createUserWithOutProfileImageUrl() {

        return User.builder()
                .id(1L)
                .loginId("loginId")
                .nickname("nickname")
                .email("email@mail.com")
                .introduction("introduction")
                .build();
    }
}
