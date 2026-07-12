package kr.mybrary.global.jwt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.auth0.jwt.JWT;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import kr.mybrary.global.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JwtUtil.class)
@TestPropertySource(properties = {
        "jwt.secretKey=testSecretKey1234567890abcdefghijk",
        "jwt.access.expiration=3600000",
        "jwt.access.header=Authorization",
        "jwt.refresh.expiration=1209600000",
        "jwt.refresh.header=Authorization-Refresh"
})
@ActiveProfiles("test")
class JwtUtilTest {

    private static final long ACCESS_EXPIRATION = 3600000L;
    private static final long REFRESH_EXPIRATION = 1209600000L;
    private static final String ACCESS_HEADER = "Authorization";
    private static final String REFRESH_HEADER = "Authorization-Refresh";
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String LOGIN_ID_CLAIM = "loginId";
    private static final String BEARER = "Bearer ";

    @Autowired
    private JwtUtil jwtUtil;

    @DisplayName("loginId를 claim에 넣고 액세스 토큰을 생성한다")
    @Test
    void createAccessToken() {
        String loginId = "loginId";
        LocalDateTime date = LocalDateTime.of(2023, 1, 1, 0, 0, 0);

        String createdAccessToken = jwtUtil.createAccessToken(loginId, date);

        assertAll(
                () -> assertThat(createdAccessToken).isNotNull(),
                () -> assertThat(JWT.decode(createdAccessToken).getSubject()).isEqualTo(ACCESS_TOKEN_SUBJECT),
                () -> assertThat(JWT.decode(createdAccessToken).getExpiresAt()).isEqualTo(new Date(
                        Date.from(date.atZone(ZoneId.systemDefault()).toInstant()).getTime() + ACCESS_EXPIRATION)),
                () -> assertThat(JWT.decode(createdAccessToken).getClaim(LOGIN_ID_CLAIM).asString()).isEqualTo(loginId),
                () -> assertThat(JWT.decode(createdAccessToken).getSignature()).isNotNull()
        );
    }

    @DisplayName("리프레시 토큰을 생성한다")
    @Test
    void createRefreshToken() {
        LocalDateTime date = LocalDateTime.of(2023, 1, 1, 0, 0, 0);

        String createdRefreshToken = jwtUtil.createRefreshToken(date);

        assertAll(
                () -> assertThat(createdRefreshToken).isNotNull(),
                () -> assertThat(JWT.decode(createdRefreshToken).getSubject()).isEqualTo(REFRESH_TOKEN_SUBJECT),
                () -> assertThat(JWT.decode(createdRefreshToken).getExpiresAt()).isEqualTo(new Date(
                        Date.from(date.atZone(ZoneId.systemDefault()).toInstant()).getTime() + REFRESH_EXPIRATION)),
                () -> assertThat(JWT.decode(createdRefreshToken).getSignature()).isNotNull()
        );
    }

    @DisplayName("액세스 토큰을 요청 헤더에서 추출한다")
    @Test
    void extractAccessToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(ACCESS_HEADER)).thenReturn(BEARER + "accessToken");

        Optional<String> accessToken = jwtUtil.extractAccessToken(request);

        assertAll(
                () -> assertThat(accessToken).isPresent(),
                () -> assertThat(accessToken.get()).isEqualTo("accessToken")
        );
    }

    @DisplayName("리프레시 토큰을 요청 헤더에서 추출한다")
    @Test
    void extractRefreshToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(REFRESH_HEADER)).thenReturn(BEARER + "refreshToken");

        Optional<String> refreshToken = jwtUtil.extractRefreshToken(request);

        assertAll(
                () -> assertThat(refreshToken).isPresent(),
                () -> assertThat(refreshToken.get()).isEqualTo("refreshToken")
        );
    }

    @DisplayName("액세스 토큰에서 loginId를 추출한다")
    @Test
    void getLoginId() {
        String loginId = "loginId";
        String accessToken = jwtUtil.createAccessToken(loginId, LocalDateTime.now());

        Optional<String> extractedLoginId = jwtUtil.getLoginIdFromValidAccessToken(accessToken);

        assertThat(extractedLoginId.get()).isEqualTo(loginId);
    }

    @DisplayName("액세스 토큰에서 loginId를 추출할 때 토큰이 유효하지 않으면 예외가 발생한다")
    @Test
    void getLoginIdWithInvalidAccessToken() {
        String accessToken = "token";

        assertThatThrownBy(() -> jwtUtil.getLoginIdFromValidAccessToken(accessToken))
                .isInstanceOf(JwtException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }

    @DisplayName("토큰의 만료 기간은 0 ~ 1시간 사이이다")
    @Test
    void getExpirationDuration() {
        String loginId = "loginId";
        LocalDateTime creationDateTime = LocalDateTime.now();
        String accessToken = jwtUtil.createAccessToken(loginId, creationDateTime);
        LocalDateTime currentDateTime = LocalDateTime.now();

        Duration expirationDuration = jwtUtil.getExpirationDuration(accessToken, currentDateTime);

        assertThat(expirationDuration).isBetween(Duration.ZERO, Duration.ofHours(1));
    }
}
