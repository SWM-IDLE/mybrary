package kr.mybrary.userservice.authentication.domain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.mybrary.userservice.global.util.JwtUtil;
import kr.mybrary.userservice.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    private static final String DIFFERENT_REFRESH_TOKEN = "저장된 리프레쉬 토큰과 다릅니다.";
    private static final int REFRESH_TOKEN_EXPIRATION = 14;

    @Override
    public void reIssueToken(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = jwtUtil.extractAccessToken(request).orElseThrow(IllegalArgumentException::new);
        String refreshToken = jwtUtil.extractRefreshToken(request).orElseThrow(IllegalArgumentException::new);

        checkRefreshToken(jwtUtil.parseLoginId(accessToken), refreshToken);
        reIssueAccessTokenAndRefreshToken(response, jwtUtil.parseLoginId(accessToken));
    }

    private void checkRefreshToken(String loginId, String refreshToken) {
        String redisRefreshToken = (String) redisUtil.get(loginId);
        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            throw new JwtException(DIFFERENT_REFRESH_TOKEN);
        }
    }

    private void reIssueAccessTokenAndRefreshToken(HttpServletResponse response, String loginId) {
        String reIssuedRefreshToken = jwtUtil.createRefreshToken(LocalDateTime.now());
        String reIssuedAccessToken = jwtUtil.createAccessToken(loginId, LocalDateTime.now());

        redisUtil.set(loginId, reIssuedRefreshToken, Duration.ofDays(REFRESH_TOKEN_EXPIRATION));
        jwtUtil.sendAccessAndRefreshToken(response, reIssuedAccessToken, reIssuedRefreshToken);
    }
}
