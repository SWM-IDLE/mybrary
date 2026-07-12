package kr.mybrary.authentication.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import kr.mybrary.RedisTestContainerConfig;
import kr.mybrary.authentication.domain.exception.InvalidRefreshTokenException;
import kr.mybrary.authentication.domain.exception.RefreshTokenExpiredException;
import kr.mybrary.global.config.RedisConfig;
import kr.mybrary.global.util.JwtUtil;
import kr.mybrary.global.util.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {RedisConfig.class, RedisUtil.class, JwtUtil.class,
                AuthenticationServiceImpl.class,
                AuthenticationServiceImplIntegrationTest.CacheConfig.class},
        initializers = RedisTestContainerConfig.class
)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "jwt.secretKey=testSecretKey1234567890abcdefghijk",
        "jwt.access.expiration=3600000",
        "jwt.access.header=Authorization",
        "jwt.refresh.expiration=1209600000",
        "jwt.refresh.header=Authorization-Refresh"
})
class AuthenticationServiceImplIntegrationTest {

    @TestConfiguration
    static class CacheConfig {
        @Bean
        CacheManager cacheManager() {
            return new NoOpCacheManager();
        }
    }

    @Autowired
    AuthenticationServiceImpl authenticationService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RedisUtil redisUtil;

    private static final String LOGIN_ID = "testUser";

    @BeforeEach
    void setUp() {
        redisUtil.delete(LOGIN_ID);
    }

    @Test
    @DisplayName("토큰 재발급 시 Redis의 Refresh Token이 새 토큰으로 교체된다.")
    void reIssueToken_replacesRefreshTokenInRedis() {
        // given — 2초 과거 시간으로 생성해야 reIssueToken 내부에서 생성된 토큰과 exp 값이 달라진다
        String originalRefreshToken = jwtUtil.createRefreshToken(LocalDateTime.now().minusSeconds(2));
        redisUtil.set(LOGIN_ID, originalRefreshToken, Duration.ofDays(14));

        String accessToken = jwtUtil.createAccessToken(LOGIN_ID, LocalDateTime.now());

        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getHeader("Authorization")).willReturn("Bearer " + accessToken);
        given(request.getHeader("Authorization-Refresh")).willReturn("Bearer " + originalRefreshToken);

        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        authenticationService.reIssueToken(request, response);

        // then
        String newRefreshToken = (String) redisUtil.get(LOGIN_ID);
        assertThat(newRefreshToken).isNotNull();
        assertThat(newRefreshToken).isNotEqualTo(originalRefreshToken);
    }

    @Test
    @DisplayName("토큰 재발급 시 응답 헤더에 새 Access Token과 Refresh Token이 포함된다.")
    void reIssueToken_returnsNewTokensInResponseHeaders() {
        // given
        String originalRefreshToken = jwtUtil.createRefreshToken(LocalDateTime.now().minusSeconds(2));
        redisUtil.set(LOGIN_ID, originalRefreshToken, Duration.ofDays(14));

        String accessToken = jwtUtil.createAccessToken(LOGIN_ID, LocalDateTime.now());

        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getHeader("Authorization")).willReturn("Bearer " + accessToken);
        given(request.getHeader("Authorization-Refresh")).willReturn("Bearer " + originalRefreshToken);

        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        authenticationService.reIssueToken(request, response);

        // then
        assertThat(response.getHeader("Authorization")).isNotNull();
        assertThat(response.getHeader("Authorization-Refresh")).isNotNull();
    }

    @Test
    @DisplayName("Redis에 Refresh Token이 없으면 RefreshTokenExpiredException이 발생한다.")
    void reIssueToken_throwsExpiredException_whenNotInRedis() {
        // given — Redis에 아무 값도 저장하지 않음
        String accessToken = jwtUtil.createAccessToken(LOGIN_ID, LocalDateTime.now());
        String refreshToken = jwtUtil.createRefreshToken(LocalDateTime.now());

        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getHeader("Authorization")).willReturn("Bearer " + accessToken);
        given(request.getHeader("Authorization-Refresh")).willReturn("Bearer " + refreshToken);

        HttpServletResponse response = mock(HttpServletResponse.class);

        // when & then
        assertThatThrownBy(() -> authenticationService.reIssueToken(request, response))
                .isInstanceOf(RefreshTokenExpiredException.class);
    }

    @Test
    @DisplayName("Redis의 Refresh Token과 요청의 Refresh Token이 다르면 InvalidRefreshTokenException이 발생한다.")
    void reIssueToken_throwsInvalidException_whenTokenMismatch() {
        // given
        String storedRefreshToken = jwtUtil.createRefreshToken(LocalDateTime.now());
        redisUtil.set(LOGIN_ID, storedRefreshToken, Duration.ofDays(14));

        String accessToken = jwtUtil.createAccessToken(LOGIN_ID, LocalDateTime.now());
        String differentRefreshToken = jwtUtil.createRefreshToken(LocalDateTime.now().minusSeconds(2));

        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getHeader("Authorization")).willReturn("Bearer " + accessToken);
        given(request.getHeader("Authorization-Refresh")).willReturn("Bearer " + differentRefreshToken);

        HttpServletResponse response = mock(HttpServletResponse.class);

        // when & then
        assertThatThrownBy(() -> authenticationService.reIssueToken(request, response))
                .isInstanceOf(InvalidRefreshTokenException.class);
    }
}
