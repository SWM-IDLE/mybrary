package kr.mybrary.userservice.authentication.domain.logout.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.mybrary.userservice.global.jwt.service.JwtService;
import kr.mybrary.userservice.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtService jwtService;
    private final RedisUtil redisUtil;

    private static final String ACCESS_TOKEN_NOT_FOUND = "로그아웃 처리할 액세스 토큰이 존재하지 않습니다.";
    private static final String LOGIN_ID_NOT_FOUND = "로그아웃 처리할 로그인 아이디가 존재하지 않습니다.";
    private static final String LOGOUT_VALUE = "logout";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 액세스 토큰 추출
        String accessToken = jwtService.extractAccessToken(request).orElseThrow(() -> new JwtException(ACCESS_TOKEN_NOT_FOUND));

        // 로그인 아이디 추출
        String loginId = jwtService.getLoginId(accessToken).orElseThrow(() -> new JwtException(LOGIN_ID_NOT_FOUND));

        // 액세스 토큰 블랙리스트 등록
        redisUtil.setBlackList(accessToken, LOGOUT_VALUE, jwtService.getExpirationDuration(accessToken, LocalDateTime.now()));

        // 로그인 아이디로 저장된 리프레쉬 토큰 삭제
        redisUtil.delete(loginId);
    }
}
