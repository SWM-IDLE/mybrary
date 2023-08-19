package kr.mybrary.userservice.authentication.domain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.mybrary.userservice.authentication.domain.exception.AccessTokenNotFoundException;
import kr.mybrary.userservice.authentication.domain.exception.InvalidRefreshTokenException;
import kr.mybrary.userservice.authentication.domain.exception.RefreshTokenExpiredException;
import kr.mybrary.userservice.authentication.domain.exception.RefreshTokenNotFoundException;
import kr.mybrary.userservice.global.util.JwtUtil;
import kr.mybrary.userservice.global.util.RedisUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    JwtUtil jwtUtil;
    @Mock
    RedisUtil redisUtil;
    @InjectMocks
    AuthenticationServiceImpl authenticationService;

    private static final String LOGIN_ID = "loginId";
    private static final String ORIGINAL_ACCESS_TOKEN = "accessToken";
    private static final String ORIGINAL_REFRESH_TOKEN = "refreshToken";
    private static final String RE_ISSUED_ACCESS_TOKEN = "reIssuedAccessToken";
    private static final String RE_ISSUED_REFRESH_TOKEN = "reIssuedRefreshToken";


    @Test
    @DisplayName("액세스 토큰과 리프레쉬 토큰을 재발급하여 응답한다.")
    void reIssueToken() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        given(jwtUtil.extractAccessToken(request)).willReturn(Optional.of(ORIGINAL_ACCESS_TOKEN));
        given(jwtUtil.extractRefreshToken(request)).willReturn(Optional.of(ORIGINAL_REFRESH_TOKEN));
        given(jwtUtil.parseLoginId(ORIGINAL_ACCESS_TOKEN)).willReturn(LOGIN_ID);
        given(redisUtil.get(LOGIN_ID)).willReturn(ORIGINAL_REFRESH_TOKEN);
        given(jwtUtil.createRefreshToken(any(LocalDateTime.class))).willReturn(RE_ISSUED_REFRESH_TOKEN);
        given(jwtUtil.createAccessToken(anyString(), any(LocalDateTime.class))).willReturn(RE_ISSUED_ACCESS_TOKEN);
        doNothing().when(redisUtil).set(anyString(), anyString(), any(Duration.class));
        doNothing().when(jwtUtil).sendAccessAndRefreshToken(response, RE_ISSUED_ACCESS_TOKEN, RE_ISSUED_REFRESH_TOKEN);

        // when
        authenticationService.reIssueToken(request, response);

        // then
        assertAll(
                () -> verify(jwtUtil, times(1)).extractAccessToken(request),
                () -> verify(jwtUtil, times(1)).extractRefreshToken(request),
                () -> verify(jwtUtil, times(2)).parseLoginId(ORIGINAL_ACCESS_TOKEN),
                () -> verify(redisUtil, times(1)).get(LOGIN_ID),
                () -> verify(jwtUtil, times(1)).createRefreshToken(any(LocalDateTime.class)),
                () -> verify(jwtUtil, times(1)).createAccessToken(anyString(), any(LocalDateTime.class)),
                () -> verify(redisUtil, times(1)).set(anyString(), anyString(), any(Duration.class)),
                () -> verify(jwtUtil, times(1)).sendAccessAndRefreshToken(response, RE_ISSUED_ACCESS_TOKEN, RE_ISSUED_REFRESH_TOKEN)
        );
    }

    @Test
    @DisplayName("액세스 토큰과 리프레쉬 토큰을 재발급할 때 액세스 토큰이 없으면 예외가 발생한다.")
    void reIssueTokenWithNoAccessToken() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        given(jwtUtil.extractAccessToken(request)).willReturn(Optional.empty());

        // when
        assertThrows(AccessTokenNotFoundException.class, () -> authenticationService.reIssueToken(request, response));

        // then
        assertAll(
                () -> verify(jwtUtil, times(1)).extractAccessToken(request),
                () -> verify(jwtUtil, never()).extractRefreshToken(request),
                () -> verify(jwtUtil, never()).parseLoginId(ORIGINAL_ACCESS_TOKEN),
                () -> verify(redisUtil, never()).get(LOGIN_ID),
                () -> verify(jwtUtil, never()).createRefreshToken(any(LocalDateTime.class)),
                () -> verify(jwtUtil, never()).createAccessToken(anyString(), any(LocalDateTime.class)),
                () -> verify(redisUtil, never()).set(anyString(), anyString(), any(Duration.class)),
                () -> verify(jwtUtil, never()).sendAccessAndRefreshToken(response, RE_ISSUED_ACCESS_TOKEN, RE_ISSUED_REFRESH_TOKEN)
        );
    }

    @Test
    @DisplayName("액세스 토큰과 리프레쉬 토큰을 재발급할 때 리프레쉬 토큰이 없으면 예외가 발생한다.")
    void reIssueTokenWithNoRefreshToken() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        given(jwtUtil.extractAccessToken(request)).willReturn(Optional.of(ORIGINAL_ACCESS_TOKEN));
        given(jwtUtil.extractRefreshToken(request)).willReturn(Optional.empty());

        // when
        assertThrows(RefreshTokenNotFoundException.class, () -> authenticationService.reIssueToken(request, response));

        // then
        assertAll(
                () -> verify(jwtUtil, times(1)).extractAccessToken(request),
                () -> verify(jwtUtil, times(1)).extractRefreshToken(request),
                () -> verify(jwtUtil, never()).parseLoginId(ORIGINAL_ACCESS_TOKEN),
                () -> verify(redisUtil, never()).get(LOGIN_ID),
                () -> verify(jwtUtil, never()).createRefreshToken(any(LocalDateTime.class)),
                () -> verify(jwtUtil, never()).createAccessToken(anyString(), any(LocalDateTime.class)),
                () -> verify(redisUtil, never()).set(anyString(), anyString(), any(Duration.class)),
                () -> verify(jwtUtil, never()).sendAccessAndRefreshToken(response, RE_ISSUED_ACCESS_TOKEN, RE_ISSUED_REFRESH_TOKEN)
        );
    }

    @Test
    @DisplayName("액세스 토큰과 리프레쉬 토큰을 재발급할 때 리프레쉬 토큰이 만료되었으면 예외가 발생한다.")
    void reIssueTokenWithExpiredRefreshToken() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        given(jwtUtil.extractAccessToken(request)).willReturn(Optional.of(ORIGINAL_ACCESS_TOKEN));
        given(jwtUtil.extractRefreshToken(request)).willReturn(Optional.of(ORIGINAL_REFRESH_TOKEN));
        given(jwtUtil.parseLoginId(ORIGINAL_ACCESS_TOKEN)).willReturn(LOGIN_ID);
        given(redisUtil.get(LOGIN_ID)).willReturn(null);

        // when
        assertThrows(RefreshTokenExpiredException.class, () -> authenticationService.reIssueToken(request, response));

        // then
        assertAll(
                () -> verify(jwtUtil, times(1)).extractAccessToken(request),
                () -> verify(jwtUtil, times(1)).extractRefreshToken(request),
                () -> verify(jwtUtil, times(1)).parseLoginId(ORIGINAL_ACCESS_TOKEN),
                () -> verify(redisUtil, times(1)).get(LOGIN_ID),
                () -> verify(jwtUtil, never()).createRefreshToken(any(LocalDateTime.class)),
                () -> verify(jwtUtil, never()).createAccessToken(anyString(), any(LocalDateTime.class)),
                () -> verify(redisUtil, never()).set(anyString(), anyString(), any(Duration.class)),
                () -> verify(jwtUtil, never()).sendAccessAndRefreshToken(response, RE_ISSUED_ACCESS_TOKEN, RE_ISSUED_REFRESH_TOKEN)
        );
    }

    @Test
    @DisplayName("액세스 토큰과 리프레쉬 토큰을 재발급할 때 리프레쉬 토큰이 유효하지 않으면 예외가 발생한다.")
    void reIssueTokenWithInvalidRefreshToken() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        given(jwtUtil.extractAccessToken(request)).willReturn(Optional.of(ORIGINAL_ACCESS_TOKEN));
        given(jwtUtil.extractRefreshToken(request)).willReturn(Optional.of("invalid_refresh_token"));
        given(jwtUtil.parseLoginId(ORIGINAL_ACCESS_TOKEN)).willReturn(LOGIN_ID);
        given(redisUtil.get(LOGIN_ID)).willReturn(ORIGINAL_REFRESH_TOKEN);

        // when
        assertThrows(InvalidRefreshTokenException.class, () -> authenticationService.reIssueToken(request, response));

        // then
        assertAll(
                () -> verify(jwtUtil, times(1)).extractAccessToken(request),
                () -> verify(jwtUtil, times(1)).extractRefreshToken(request),
                () -> verify(jwtUtil, times(1)).parseLoginId(ORIGINAL_ACCESS_TOKEN),
                () -> verify(redisUtil, times(1)).get(LOGIN_ID),
                () -> verify(jwtUtil, never()).createRefreshToken(any(LocalDateTime.class)),
                () -> verify(jwtUtil, never()).createAccessToken(anyString(), any(LocalDateTime.class)),
                () -> verify(redisUtil, never()).set(anyString(), anyString(), any(Duration.class)),
                () -> verify(jwtUtil, never()).sendAccessAndRefreshToken(response, RE_ISSUED_ACCESS_TOKEN, RE_ISSUED_REFRESH_TOKEN)
        );
    }

}