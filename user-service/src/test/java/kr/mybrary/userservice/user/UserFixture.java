package kr.mybrary.userservice.user;

import java.util.Collections;
import java.util.List;
import kr.mybrary.userservice.user.persistence.Follow;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.SocialType;
import kr.mybrary.userservice.user.persistence.User;

public enum UserFixture {

    USER(1L, "loginId", "nickname", "encodedPassword", Role.USER, "socialId", SocialType.GOOGLE,
            "refreshToken", "email@mail.com", "introduction", "profileImageUrl",
            Collections.emptyList(), Collections.emptyList()),
    USER_WITHOUT_PROFILE_IMAGE_URL(1L, "loginId", "nickname", "encodedPassword", Role.USER,
            "socialId",
            SocialType.GOOGLE, "refreshToken", "email@mail.com", "introduction", null,
            Collections.emptyList(),
            Collections.emptyList());

    private final Long id;
    private final String loginId;
    private final String nickname;
    private final String password;
    private final Role role;
    private final String socialId;
    private final SocialType socialType;
    private final String refreshToken;
    private final String email;
    private final String introduction;
    private final String profileImageUrl;
    private final List<Follow> followers;
    private final List<Follow> followings;

    UserFixture(Long id, String loginId, String nickname, String password, Role role,
            String socialId, SocialType socialType, String refreshToken, String email,
            String introduction, String profileImageUrl, List<Follow> followers,
            List<Follow> followings) {
        this.id = id;
        this.loginId = loginId;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.socialId = socialId;
        this.socialType = socialType;
        this.refreshToken = refreshToken;
        this.email = email;
        this.introduction = introduction;
        this.profileImageUrl = profileImageUrl;
        this.followers = followers;
        this.followings = followings;
    }

    public User getUser() {
        return User.builder()
                .id(id)
                .loginId(loginId)
                .nickname(nickname)
                .password(password)
                .role(role)
                .socialId(socialId)
                .socialType(socialType)
                .refreshToken(refreshToken)
                .email(email)
                .introduction(introduction)
                .profileImageUrl(profileImageUrl)
                .followers(followers)
                .followings(followings)
                .build();
    }

}