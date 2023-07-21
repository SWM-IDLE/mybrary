package kr.mybrary.userservice.authentication.domain.login.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.Duration;
import java.util.Date;
import kr.mybrary.userservice.global.jwt.service.JwtService;
import kr.mybrary.userservice.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final RedisUtil redisUtil;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;
    private static final int REFRESH_TOKEN_EXPIRATION = 14;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
        String loginId = extractUsername(authentication);
        String accessToken = jwtService.createAccessToken(loginId, new Date());
        String refreshToken = jwtService.createRefreshToken(new Date());

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        redisUtil.set(loginId, refreshToken, Duration.ofDays(REFRESH_TOKEN_EXPIRATION));

        log.info("로그인에 성공하였습니다. 로그인 아이디 : {}", loginId);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);

    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

}
