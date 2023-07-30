package kr.mybrary.userservice.authentication.domain.oauth2;

import java.util.Map;
import java.util.UUID;
import kr.mybrary.userservice.authentication.domain.oauth2.userinfo.GoogleOAuth2UserInfo;
import kr.mybrary.userservice.authentication.domain.oauth2.userinfo.KakaoOAuth2UserInfo;
import kr.mybrary.userservice.authentication.domain.oauth2.userinfo.NaverOAuth2UserInfo;
import kr.mybrary.userservice.authentication.domain.oauth2.userinfo.OAuth2UserInfo;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.SocialType;
import kr.mybrary.userservice.user.persistence.User;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

// 소셜 별로 받는 데이터를 분기 처리하는 DTO 클래스
@Getter
public class OAuthAttributes {

    // OAuth2 로그인 진행 시 키가 되는 필드 값
    private String nameAttributeKey;
    private OAuth2UserInfo oAuth2UserInfo;

    private static final String SOCIAL_TYPE_NOT_SUPPORTED = "지원하지 않는 소셜 로그인입니다.";

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
        if (socialType == SocialType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        if (socialType == SocialType.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        }
        throw new OAuth2AuthenticationException(SOCIAL_TYPE_NOT_SUPPORTED);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName,
            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName,
            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    public User toEntity(SocialType socialType, OAuth2UserInfo oAuth2UserInfo) {
        return User.builder()
                .socialType(socialType)
                .socialId(oAuth2UserInfo.getId())
                .loginId(UUID.randomUUID().toString())
                .password(UUID.randomUUID().toString())
                .email(oAuth2UserInfo.getEmail())
                .nickname(oAuth2UserInfo.getNickname() + RandomStringUtils.randomNumeric(5))
                .profileImageUrl(oAuth2UserInfo.getImageUrl())
                .introduction("")
                .role(Role.USER)
                .build();
    }


}
