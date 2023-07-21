package kr.mybrary.userservice.authentication.domain.logout.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.mybrary.userservice.authentication.domain.logout.exception.InvalidLogoutRequest;
import kr.mybrary.userservice.global.jwt.service.JwtService;
import kr.mybrary.userservice.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtService jwtService;
    private final RedisUtil redisUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 액세스 토큰 추출
        String accessToken = jwtService.extractAccessToken(request).orElseThrow(InvalidLogoutRequest::new);

        // 로그인 아이디 추출
        String loginId = jwtService.getLoginId(accessToken).orElseThrow(InvalidLogoutRequest::new);

        // 액세스 토큰 블랙리스트 등록
        redisUtil.setBlackList(accessToken, "logout", jwtService.getExpirationDuration(accessToken, LocalDateTime.now()));

        // 로그인 아이디로 저장된 리프레쉬 토큰 삭제
        redisUtil.delete(loginId);
    }
}
