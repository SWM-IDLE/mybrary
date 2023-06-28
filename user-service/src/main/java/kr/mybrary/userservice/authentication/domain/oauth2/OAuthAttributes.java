package kr.mybrary.userservice.authentication.domain.oauth2;

import java.util.Map;
import java.util.UUID;
import kr.mybrary.userservice.authentication.domain.oauth2.userinfo.GoogleOAuth2UserInfo;
import kr.mybrary.userservice.authentication.domain.oauth2.userinfo.OAuth2UserInfo;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.SocialType;
import kr.mybrary.userservice.user.persistence.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes { // 소셜 별로 받는 데이터를 분기 처리하는 DTO 클래스

    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값
    private OAuth2UserInfo oAuth2UserInfo;

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    public static OAuthAttributes of(SocialType socialType, String userNameAttributeName,
            Map<String, Object> attributes) {
        if (socialType == SocialType.GOOGLE) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        throw new IllegalArgumentException("지원하지 않는 소셜 타입입니다.");
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    private User toEntity(SocialType socialType, OAuth2UserInfo oAuth2UserInfo) {
        return User.builder()
                .socialType(socialType)
                .socialId(oAuth2UserInfo.getId())
                .loginId(UUID.randomUUID().toString())
                .email(UUID.randomUUID() + "@socialUser.com")
                .nickname(oAuth2UserInfo.getNickname())
                .profileImageUrl(oAuth2UserInfo.getImageUrl())
                .role(Role.GUEST)
                .build();
    }


}
