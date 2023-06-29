package kr.mybrary.userservice.authentication.domain.oauth2.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import kr.mybrary.userservice.authentication.domain.AuthenticationService;
import kr.mybrary.userservice.authentication.domain.oauth2.CustomOAuth2User;
import kr.mybrary.userservice.global.jwt.service.JwtService;
import kr.mybrary.userservice.user.persistence.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    static final String ENCODING_UTF_8 = "UTF-8";
    static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        log.info("OAuth2 Login Success Handler 실행 - OAuth2 로그인 성공");

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        if (oAuth2User.getRole() == Role.GUEST) {
            initialLoginSuccess(response, oAuth2User);
            return;
        }
        loginSuccess(response, oAuth2User);

    }

    private void initialLoginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User)
            throws IOException {
        String accessToken = jwtService.createAccessToken(oAuth2User.getLoginId(), new Date());
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.setCharacterEncoding(ENCODING_UTF_8);
        response.setContentType(CONTENT_TYPE_TEXT_PLAIN);
        response.getWriter().write("회원 가입이 완료되었습니다.");
        jwtService.sendAccessAndRefreshToken(response, accessToken, null);
        // 추가 정보 입력 필요 시 회원 가입 추가 정보 입력 페이지로 리다이렉트 후 USER 권한 부여
        authenticationService.authorizeUser(oAuth2User.getLoginId());
        log.info("OAuth2 Login Success Handler : initialLoginSuccess 실행 - 회원 가입 성공");
    }

    // TODO : 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) {
        String accessToken = jwtService.createAccessToken(oAuth2User.getLoginId(), new Date());
        String refreshToken = jwtService.createRefreshToken(new Date());
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getLoginId(), refreshToken);
    }


}
