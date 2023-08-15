package kr.mybrary.userservice.global.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.mybrary.userservice.global.jwt.service.JwtService;
import kr.mybrary.userservice.global.redis.RedisUtil;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String USER_SERVICE_API_URL = "/api/v1";
    private static final String OAUTH2_URL = "/oauth2/authorization";
    private static final String OAUTH2_REDIRECT_URL = "/login/oauth2/code";
    private static final String ACTUATOR_URL = "/actuator";
    private static final List<String> TOKEN_AUTH_WHITELIST = List.of(USER_SERVICE_API_URL, OAUTH2_URL, OAUTH2_REDIRECT_URL, ACTUATOR_URL);
    private static final String TOKEN_LOGOUT = "로그아웃된 토큰입니다.";
    private static final String DIFFERENT_REFRESH_TOKEN = "저장된 리프레쉬 토큰과 다릅니다.";
    private static final String USER_NOT_FOUND_BY_LOGIN_ID = "존재하지 않는 유저입니다.";
    private static final int REFRESH_TOKEN_EXPIRATION = 14;

    private final JwtService jwtService;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (tokenAuthenticationNotRequired(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtService.extractAccessToken(request).orElse(null);

        jwtService.extractRefreshToken(request).ifPresentOrElse(refreshToken -> {
                    System.out.println("refreshToken = " + refreshToken);
                    checkRefreshToken(jwtService.parseLoginId(accessToken), refreshToken);
                    reIssueAccessTokenAndRefreshToken(response, jwtService.parseLoginId(accessToken));
                },
                () -> {
                    checkIfTokenIsLogout(accessToken);
                    String loginId = jwtService.getLoginIdFromValidAccessToken(accessToken).orElse(null);
                    saveAuthentication(getUserWithAccessToken(accessToken));
                    request.setAttribute("USER-ID", loginId);
                    continueFilter(request, response, filterChain);
                }
        );
    }

    private void continueFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try { // TODO: 예외 처리 수정
            filterChain.doFilter(request, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean tokenAuthenticationNotRequired(HttpServletRequest request) {
        return TOKEN_AUTH_WHITELIST.stream()
                .anyMatch(uri -> request.getRequestURI().contains(uri));
    }

    private void checkIfTokenIsLogout(String accessToken) {
        if (accessToken != null && redisUtil.get(accessToken) != null) {
            throw new JwtException(TOKEN_LOGOUT);
        }
    }

    private void checkRefreshToken(String loginId, String refreshToken) {
        String redisRefreshToken = (String) redisUtil.get(loginId);
        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            throw new JwtException(DIFFERENT_REFRESH_TOKEN);
        }
    }

    private void reIssueAccessTokenAndRefreshToken(HttpServletResponse response, String loginId) {
        String reIssuedRefreshToken = jwtService.createRefreshToken(LocalDateTime.now());
        String reIssuedAccessToken = jwtService.createAccessToken(loginId, LocalDateTime.now());

        redisUtil.set(loginId, reIssuedRefreshToken, Duration.ofDays(REFRESH_TOKEN_EXPIRATION));
        jwtService.sendAccessAndRefreshToken(response, reIssuedAccessToken, reIssuedRefreshToken);
    }

    private User getUserWithAccessToken(String accessToken) {
        return userRepository.findByLoginId(jwtService.getLoginIdFromValidAccessToken(accessToken).orElse(null))
                .orElseThrow(() -> new JwtException(USER_NOT_FOUND_BY_LOGIN_ID));
    }

    private void saveAuthentication(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getLoginId())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
