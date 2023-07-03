package kr.mybrary.userservice.global.jwt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class JwtServiceTest {

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

    @Autowired
    private JwtService jwtService;

    @DisplayName("loginId를 claim에 넣고 액세스 토큰을 생성한다")
    @Test
    void createAccessToken() {
        // given
        String loginId = "loginId";
        Date date = new Date(2023, 1, 1, 0, 0, 0);

        // when
        String createdAccessToken = jwtService.createAccessToken(loginId, date);

        // then
        assertAll(
                () -> assertThat(createdAccessToken).isNotNull(),
                () -> assertThat(JWT.decode(createdAccessToken).getSubject()).isEqualTo(ACCESS_TOKEN_SUBJECT),
                () -> assertThat(JWT.decode(createdAccessToken).getExpiresAt()).isEqualTo(new Date(date.getTime() + accessTokenExpirationPeriod)),
                () -> assertThat(JWT.decode(createdAccessToken).getClaim(LOGIN_ID_CLAIM).asString()).isEqualTo(loginId),
                () -> assertThat(JWT.decode(createdAccessToken).getSignature()).isNotNull()
        );
    }

    @DisplayName("리프레시 토큰을 생성한다")
    @Test
    void createRefreshToken() {
        // given
        Date date = new Date(2023, 1, 1, 0, 0, 0);

        // when
        String createdAccessToken = jwtService.createRefreshToken(date);

        // then
        assertAll(
                () -> assertThat(createdAccessToken).isNotNull(),
                () -> assertThat(JWT.decode(createdAccessToken).getSubject()).isEqualTo(REFRESH_TOKEN_SUBJECT),
                () -> assertThat(JWT.decode(createdAccessToken).getExpiresAt()).isEqualTo(new Date(date.getTime() + refreshTokenExpirationPeriod)),
                () -> assertThat(JWT.decode(createdAccessToken).getSignature()).isNotNull()
        );
    }

    @DisplayName("액세스 토큰을 요청 헤더에서 추출한다")
    @Test
    void extractAccessToken() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(accessHeader)).thenReturn(BEARER + "accessToken");

        // when
        Optional<String> accessToken = jwtService.extractAccessToken(request);

        // then
        assertAll(
                () -> assertThat(accessToken).isPresent(),
                () -> assertThat(accessToken.get()).isEqualTo("accessToken")
        );

    }

    @DisplayName("리프레시 토큰을 요청 헤더에서 추출한다")
    @Test
    void extractRefreshToken() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(refreshHeader)).thenReturn(BEARER + "refreshToken");

        // when
        Optional<String> refreshToken = jwtService.extractRefreshToken(request);

        // then
        assertAll(
                () -> assertThat(refreshToken).isPresent(),
                () -> assertThat(refreshToken.get()).isEqualTo("refreshToken")
        );
    }

    @DisplayName("액세스 토큰에서 loginId를 추출한다")
    @Test
    void extractLoginId() {
        // given
        String loginId = "loginId";
        String accessToken = jwtService.createAccessToken(loginId, new Date());

        // when
        Optional<String> extractedLoginId = jwtService.extractLoginId(accessToken);

        // then
        assertThat(extractedLoginId.get()).isEqualTo(loginId);
    }

    @DisplayName("토큰의 유효성을 검증한다")
    @Test
    void isTokenValid() {
        // given
        String token = JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .sign(Algorithm.HMAC512(secretKey));

        // when
        boolean isValid = jwtService.isTokenValid(token);

        // then
        assertThat(isValid).isTrue();
    }

    @DisplayName("토큰이 유효하지 않으면 JwtException이 발생한다")
    @Test
    void isTokenNotValid() {
        // given
        String token = "token";

        // when then
        assertThatThrownBy(() -> jwtService.isTokenValid(token))
                .isInstanceOf(JwtException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }

}