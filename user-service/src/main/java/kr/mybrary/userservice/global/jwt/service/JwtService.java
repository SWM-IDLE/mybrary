package kr.mybrary.userservice.global.jwt.service;

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
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
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
public class JwtService {

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

    private final UserRepository userRepository;

    // AccessToken 생성 - loginId을 payload로 넣어서 생성
    public String createAccessToken(String loginId, LocalDateTime now) {
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(getTimeFrom(now) + accessTokenExpirationPeriod))
                .withClaim(LOGIN_ID_CLAIM, loginId)
                .sign(Algorithm.HMAC512(secretKey));
    }

    // RefreshToken 생성
    public String createRefreshToken(LocalDateTime now) {
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(getTimeFrom(now) + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    private static long getTimeFrom(LocalDateTime now) {
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant()).getTime();
    }

    // AccessToken + RefreshToken 헤더에 추가
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);
        log.info("발급된 Access Token : {}", accessToken);
        log.info("발급된 Refresh Token : {}", refreshToken);
    }

    // 헤더에서 AccessToken 추출
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    // 헤더에서 RefreshToken 추출
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    // AccessToken에서 LoginId 추출
    public Optional<String> getLoginId(String accessToken) {
        validateToken(accessToken);
        return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(accessToken)
                .getClaim(LOGIN_ID_CLAIM)
                .asString());
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
