package kr.mybrary.userservice.authentication.domain.oauth2.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import kr.mybrary.userservice.authentication.domain.oauth2.CustomOAuth2User;
import kr.mybrary.userservice.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    static final String CALLBACK_URL = "kr.mybrary://";
    static final String ACCESS_TOKEN_PARAMETER = "Authorization";
    static final String REFRESH_TOKEN_PARAMETER = "Authorization-Refresh";

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        log.info("OAuth2 Login Success Handler 실행 - OAuth2 로그인 성공");

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String accessToken = jwtService.createAccessToken(oAuth2User.getLoginId(), new Date());
        String refreshToken = jwtService.createRefreshToken(new Date());
        jwtService.updateRefreshToken(oAuth2User.getLoginId(), refreshToken);


        String url = UriComponentsBuilder.fromUriString(CALLBACK_URL)
                .queryParam(ACCESS_TOKEN_PARAMETER, accessToken)
                .queryParam(REFRESH_TOKEN_PARAMETER, refreshToken)
                .toUriString();

        response.sendRedirect(url);
        log.info("Redirect Url: " + url);
    }

}
