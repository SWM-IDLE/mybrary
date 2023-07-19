package kr.mybrary.userservice.user;

import kr.mybrary.userservice.user.persistence.Follow;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.SocialType;
import kr.mybrary.userservice.user.persistence.User;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public enum UserFixture {

    COMMON_USER(1L, "loginId", "nickname", "encodedPassword", Role.USER, "socialId",
            SocialType.GOOGLE, "email@mail.com", "introduction", "profileImageUrl",
            Collections.emptyList(), Collections.emptyList()),
    USER_WITHOUT_PROFILE_IMAGE_URL(1L, "loginId", "nickname", "encodedPassword", Role.USER,
            "socialId", SocialType.GOOGLE, "email@mail.com", "introduction", null,
            Collections.emptyList(), Collections.emptyList()),

    USER_WITHOUT_EMAIL(1L, "loginId", "nickname", "encodedPassword", Role.USER,
            "socialId", SocialType.GOOGLE, null, "introduction", "profileImageUrl",
            Collections.emptyList(), Collections.emptyList()),

    USER_WITH_FOLLOWER(1L, "followingId", "nickname", "encodedPassword", Role.USER,
            "socialId", SocialType.GOOGLE, "email@mail.com", "introduction", "profileImageUrl",
            List.of(Follow.builder().source(User.builder().loginId("followerId").build()).target(User.builder().loginId("followingId").build()).build()),
            Collections.emptyList()),

    USER_AS_FOLLOWER(1L, "followerId", "nickname", "encodedPassword", Role.USER,
            "socialId", SocialType.GOOGLE, "email@mail.com", "introduction", "profileImageUrl",
            Collections.emptyList(),
            List.of(Follow.builder().source(User.builder().loginId("followerId").build()).target(User.builder().loginId("followingId").build()).build()));

    private final Long id;
    private final String loginId;
    private final String nickname;
    private final String password;
    private final Role role;
    private final String socialId;
    private final SocialType socialType;
    private final String email;
    private final String introduction;
    private final String profileImageUrl;
    private final List<Follow> followers;
    private final List<Follow> followings;

    public User getUser() {
        return User.builder()
                .id(id)
                .loginId(loginId)
                .nickname(nickname)
                .password(password)
                .role(role)
                .socialId(socialId)
                .socialType(socialType)
                .email(email)
                .introduction(introduction)
                .profileImageUrl(profileImageUrl)
                .followers(followers)
                .followings(followings)
                .build();
    }

}