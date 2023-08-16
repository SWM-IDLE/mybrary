package kr.mybrary.userservice.global.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtUtil {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String LOGIN_ID_CLAIM = "loginId";
    private static final String BEARER = "Bearer ";
    private static final String INVALID_TOKEN_MESSAGE = "유효하지 않은 토큰입니다.";

    public String createAccessToken(String loginId, LocalDateTime now) {
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(getTimeFrom(now) + accessTokenExpirationPeriod))
                .withClaim(LOGIN_ID_CLAIM, loginId)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken(LocalDateTime now) {
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(getTimeFrom(now) + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    private static long getTimeFrom(LocalDateTime now) {
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant()).getTime();
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);
        log.info("발급된 Access Token : {}", accessToken);
        log.info("발급된 Refresh Token : {}", refreshToken);
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> getLoginIdFromValidAccessToken(String accessToken) {
        validateToken(accessToken);
        return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(accessToken)
                .getClaim(LOGIN_ID_CLAIM)
                .asString());
    }

    public String parseLoginId(String accessToken) {
        String loginId = JWT.decode(accessToken).getClaim(LOGIN_ID_CLAIM).asString();
        return loginId;
    }

    public Duration getExpirationDuration(String accessToken, LocalDateTime now) {
        return Duration.ofMillis(getExpiration(accessToken).getTime() - getTimeFrom(now));
    }

    private Date getExpiration(String accessToken) {
        validateToken(accessToken);
        return JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(accessToken)
                .getExpiresAt();
    }

    private void validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(token);
        } catch (Exception e) {
            log.error(INVALID_TOKEN_MESSAGE);
            throw new JwtException(INVALID_TOKEN_MESSAGE);
        }
    }

}
