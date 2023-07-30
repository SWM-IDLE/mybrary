package kr.mybrary.userservice.authentication.domain.oauth;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OAuth2Test {

    @Autowired
    MockMvc mockMvc;

    static final String OAUTH2_URL = "/oauth2/authorization";

    @DisplayName("Google 로그인 시도 시 Google OAuth 인증 창이 나타난다")
    @Test
    void googleLogin() throws Exception {
        // given
        String googleOAuth2Url = OAUTH2_URL + "/google";
        String redirectedGoogleOAuth2Url = "https://accounts.google.com/o/oauth2/v2/auth";

        // when // then
        mockMvc.perform(post(googleOAuth2Url))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location",containsString(redirectedGoogleOAuth2Url)));
    }


    @DisplayName("Kakao 로그인 시도 시 Kakao OAuth 인증 창이 나타난다")
    @Test
    void kakaoLogin() throws Exception {
        // given
        String kakaoOAuth2Url = OAUTH2_URL + "/kakao";
        String redirectedKakaoOAuth2Url = "https://kauth.kakao.com/oauth/authorize";

        // when // then
        mockMvc.perform(post(kakaoOAuth2Url))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location",containsString(redirectedKakaoOAuth2Url)));
    }

    @DisplayName("Naver 로그인 시도 시 Naver OAuth 인증 창이 나타난다")
    @Test
    void naverLogin() throws Exception {
        // given
        String naverOAuth2Url = OAUTH2_URL + "/naver";
        String redirectedNaverOAuth2Url = "https://nid.naver.com/oauth2.0/authorize";

        // when // then
        mockMvc.perform(post(naverOAuth2Url))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location",containsString(redirectedNaverOAuth2Url)));
    }

}
