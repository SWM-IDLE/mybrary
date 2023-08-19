package kr.mybrary.userservice.authentication.domain.oauth2.service;

import java.util.Collections;
import java.util.Map;
import kr.mybrary.userservice.authentication.domain.oauth2.CustomOAuth2User;
import kr.mybrary.userservice.authentication.domain.oauth2.OAuthAttributes;
import kr.mybrary.userservice.user.persistence.SocialType;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static kr.mybrary.userservice.global.constant.ImageConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String GOOGLE = "google";
    private static final String KAKAO = "kakao";
    private static final String NAVER = "naver";
    private static final String SOCIAL_TYPE_NOT_SUPPORTED = "지원하지 않는 소셜 로그인입니다.";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        /* DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서
        사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
        결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저 */
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // userRequest에서 registrationId, socialType, userNameAttributeName을 가져온다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // socialType에 따라 유저 정보를 통해 OAuthAttributes 객체를 생성한다.
        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName,
                attributes);

        // OAuthAttributes 객체와 SocialType를 통해 User 객체를 생성한다.
        User createdUser = getUser(extractAttributes, socialType);

        // DefaultOAuth2User를 구현한 CustomOAuth2User 객체를 생성하여 반환한다.
        return new CustomOAuth2User(
                Collections.singleton(
                        new SimpleGrantedAuthority(createdUser.getRole().getDescription())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdUser.getLoginId(),
                createdUser.getRole()
        );

    }

    private SocialType getSocialType(String registrationId) {
        if (registrationId.equals(GOOGLE)) {
            return SocialType.GOOGLE;
        }
        if (registrationId.equals(KAKAO)) {
            return SocialType.KAKAO;
        }
        if (registrationId.equals(NAVER)) {
            return SocialType.NAVER;
        }
        throw new OAuth2AuthenticationException(SOCIAL_TYPE_NOT_SUPPORTED);
    }

    private User getUser(OAuthAttributes attributes, SocialType socialType) {
        User findUser = userRepository.findBySocialTypeAndSocialId(socialType,
                attributes.getOAuth2UserInfo().getId()).orElse(null);

        if (findUser == null) {
            return saveUser(attributes, socialType);
        }
        return findUser;
    }

    private User saveUser(OAuthAttributes attributes, SocialType socialType) {
        User createdUser = attributes.toEntity(socialType, attributes.getOAuth2UserInfo());
        createdUser.updatePassword(passwordEncoder.encode(createdUser.getPassword()));
        setDefaultProfileImage(createdUser);
        return userRepository.save(createdUser);
    }

    private void setDefaultProfileImage(User createdUser) {
        createdUser.updateProfileImageUrl(DEFAULT_PROFILE_IMAGE.getUrl());
        createdUser.updateProfileImageThumbnailTinyUrl(DEFAULT_PROFILE_IMAGE_TINY.getUrl());
        createdUser.updateProfileImageThumbnailSmallUrl(DEFAULT_PROFILE_IMAGE_SMALL.getUrl());
    }

}
