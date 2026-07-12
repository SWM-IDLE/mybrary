package kr.mybrary.authentication.domain.logout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.Duration;
import kr.mybrary.RedisTestContainerConfig;
import kr.mybrary.authentication.domain.logout.handler.CustomLogoutHandler;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {RedisConfig.class, RedisUtil.class, JwtUtil.class,
                CustomLogoutHandlerIntegrationTest.CacheConfig.class},
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
class CustomLogoutHandlerIntegrationTest {

    @TestConfiguration
    static class CacheConfig {
        @Bean
        CacheManager cacheManager() {
            return new NoOpCacheManager();
        }
    }

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RedisUtil redisUtil;

    CustomLogoutHandler customLogoutHandler;

    private static final String LOGIN_ID = "testUser";
    private static final String LOGOUT_VALUE = "logout";

    @BeforeEach
    void setUp() {
        customLogoutHandler = new CustomLogoutHandler(jwtUtil, redisUtil);
        // Redis 상태 초기화
        redisUtil.delete(LOGIN_ID);
    }

    @Test
    @DisplayName("로그아웃 시 액세스 토큰이 Redis 블랙리스트에 등록된다.")
    void logout_storesAccessTokenInRedisBlacklist() {
        // given
        String accessToken = jwtUtil.createAccessToken(LOGIN_ID, LocalDateTime.now());
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        given(request.getHeader("Authorization")).willReturn("Bearer " + accessToken);

        // when
        customLogoutHandler.logout(request, response, null);

        // then
        assertThat(redisUtil.get(accessToken)).isEqualTo(LOGOUT_VALUE);
    }

    @Test
    @DisplayName("로그아웃 시 loginId에 저장된 Refresh Token이 Redis에서 삭제된다.")
    void logout_deletesRefreshTokenFromRedis() {
        // given
        String refreshToken = jwtUtil.createRefreshToken(LocalDateTime.now());
        redisUtil.set(LOGIN_ID, refreshToken, Duration.ofDays(14));

        String accessToken = jwtUtil.createAccessToken(LOGIN_ID, LocalDateTime.now());
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        given(request.getHeader("Authorization")).willReturn("Bearer " + accessToken);

        // when
        customLogoutHandler.logout(request, response, null);

        // then
        assertThat(redisUtil.get(LOGIN_ID)).isNull();
    }

    @Test
    @DisplayName("로그아웃 후 블랙리스트에 등록된 액세스 토큰은 TTL이 설정되어 있다.")
    void logout_blacklistedToken_hasTtl() {
        // given
        String accessToken = jwtUtil.createAccessToken(LOGIN_ID, LocalDateTime.now());
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        given(request.getHeader("Authorization")).willReturn("Bearer " + accessToken);

        // when
        customLogoutHandler.logout(request, response, null);

        // then
        // 블랙리스트에 등록됐고 값이 "logout"인지 확인
        assertThat(redisUtil.get(accessToken)).isEqualTo(LOGOUT_VALUE);
    }
}
