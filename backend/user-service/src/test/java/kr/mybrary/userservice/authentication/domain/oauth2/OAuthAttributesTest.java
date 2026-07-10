package kr.mybrary.userservice.authentication.domain.oauth2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.Map;
import kr.mybrary.userservice.authentication.domain.oauth2.userinfo.GoogleOAuth2UserInfo;
import kr.mybrary.userservice.authentication.domain.oauth2.userinfo.KakaoOAuth2UserInfo;
import kr.mybrary.userservice.authentication.domain.oauth2.userinfo.NaverOAuth2UserInfo;
import kr.mybrary.userservice.user.persistence.SocialType;
import kr.mybrary.userservice.user.persistence.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

class OAuthAttributesTest {

    @Test
    @DisplayName("Google 소셜 타입으로 OAuthAttributes를 생성한다.")
    void of_google_returnsGoogleOAuthAttributes() {
        Map<String, Object> attributes = Map.of(
                "sub", "google-user-id",
                "name", "테스트유저",
                "email", "test@gmail.com",
                "picture", "https://example.com/pic.jpg"
        );

        OAuthAttributes result = OAuthAttributes.of(SocialType.GOOGLE, "sub", attributes);

        assertThat(result.getNameAttributeKey()).isEqualTo("sub");
        assertThat(result.getOAuth2UserInfo()).isInstanceOf(GoogleOAuth2UserInfo.class);
        assertThat(result.getOAuth2UserInfo().getId()).isEqualTo("google-user-id");
        assertThat(result.getOAuth2UserInfo().getNickname()).isEqualTo("테스트유저");
        assertThat(result.getOAuth2UserInfo().getEmail()).isEqualTo("test@gmail.com");
        assertThat(result.getOAuth2UserInfo().getImageUrl()).isEqualTo("https://example.com/pic.jpg");
    }

    @Test
    @DisplayName("Kakao 소셜 타입으로 OAuthAttributes를 생성한다.")
    void of_kakao_returnsKakaoOAuthAttributes() {
        Map<String, Object> profile = new HashMap<>();
        profile.put("nickname", "카카오유저");
        profile.put("profile_image_url", "https://kakao.img/profile.jpg");

        Map<String, Object> kakaoAccount = new HashMap<>();
        kakaoAccount.put("email", "test@kakao.com");
        kakaoAccount.put("profile", profile);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", 12345L);
        attributes.put("kakao_account", kakaoAccount);

        OAuthAttributes result = OAuthAttributes.of(SocialType.KAKAO, "id", attributes);

        assertThat(result.getNameAttributeKey()).isEqualTo("id");
        assertThat(result.getOAuth2UserInfo()).isInstanceOf(KakaoOAuth2UserInfo.class);
        assertThat(result.getOAuth2UserInfo().getId()).isEqualTo("12345");
        assertThat(result.getOAuth2UserInfo().getNickname()).isEqualTo("카카오유저");
        assertThat(result.getOAuth2UserInfo().getEmail()).isEqualTo("test@kakao.com");
        assertThat(result.getOAuth2UserInfo().getImageUrl()).isEqualTo("https://kakao.img/profile.jpg");
    }

    @Test
    @DisplayName("Naver 소셜 타입으로 OAuthAttributes를 생성한다.")
    void of_naver_returnsNaverOAuthAttributes() {
        Map<String, Object> response = new HashMap<>();
        response.put("id", "naver-user-id");
        response.put("nickname", "네이버유저");
        response.put("email", "test@naver.com");
        response.put("profile_image", "https://naver.img/profile.jpg");

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("response", response);

        OAuthAttributes result = OAuthAttributes.of(SocialType.NAVER, "response", attributes);

        assertThat(result.getNameAttributeKey()).isEqualTo("response");
        assertThat(result.getOAuth2UserInfo()).isInstanceOf(NaverOAuth2UserInfo.class);
        assertThat(result.getOAuth2UserInfo().getId()).isEqualTo("naver-user-id");
        assertThat(result.getOAuth2UserInfo().getNickname()).isEqualTo("네이버유저");
        assertThat(result.getOAuth2UserInfo().getEmail()).isEqualTo("test@naver.com");
        assertThat(result.getOAuth2UserInfo().getImageUrl()).isEqualTo("https://naver.img/profile.jpg");
    }

    @Test
    @DisplayName("지원하지 않는 소셜 타입은 OAuth2AuthenticationException을 발생시킨다.")
    void of_unsupportedSocialType_throwsException() {
        // APPLE is not supported
        assertThatThrownBy(() ->
                OAuthAttributes.of(SocialType.APPLE, "id", Map.of()))
                .isInstanceOf(OAuth2AuthenticationException.class);
    }

    @Test
    @DisplayName("toEntity는 OAuth2 정보로 User 엔티티를 생성한다.")
    void toEntity_createsUserEntityFromOAuthUserInfo() {
        Map<String, Object> attributes = Map.of(
                "sub", "google-id",
                "name", "구글유저",
                "email", "google@gmail.com",
                "picture", "https://google.com/pic.jpg"
        );
        OAuthAttributes oAuthAttributes = OAuthAttributes.of(SocialType.GOOGLE, "sub", attributes);

        User user = oAuthAttributes.toEntity(SocialType.GOOGLE, oAuthAttributes.getOAuth2UserInfo());

        assertThat(user.getSocialType()).isEqualTo(SocialType.GOOGLE);
        assertThat(user.getSocialId()).isEqualTo("google-id");
        assertThat(user.getEmail()).isEqualTo("google@gmail.com");
        assertThat(user.getLoginId()).isNotNull();
        assertThat(user.getNickname()).startsWith("구글유저");
    }

    @Test
    @DisplayName("Kakao kakao_account가 없으면 nickname과 email은 null을 반환한다.")
    void kakao_missingKakaoAccount_returnsNullNicknameAndEmail() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", 99L);

        OAuthAttributes result = OAuthAttributes.of(SocialType.KAKAO, "id", attributes);

        assertThat(result.getOAuth2UserInfo().getNickname()).isNull();
        assertThat(result.getOAuth2UserInfo().getEmail()).isNull();
        assertThat(result.getOAuth2UserInfo().getImageUrl()).isNull();
    }

    @Test
    @DisplayName("Naver response가 없으면 id, nickname, email은 null을 반환한다.")
    void naver_missingResponse_returnsNullFields() {
        Map<String, Object> attributes = new HashMap<>();

        OAuthAttributes result = OAuthAttributes.of(SocialType.NAVER, "response", attributes);

        assertThat(result.getOAuth2UserInfo().getId()).isNull();
        assertThat(result.getOAuth2UserInfo().getNickname()).isNull();
        assertThat(result.getOAuth2UserInfo().getEmail()).isNull();
        assertThat(result.getOAuth2UserInfo().getImageUrl()).isNull();
    }
}
