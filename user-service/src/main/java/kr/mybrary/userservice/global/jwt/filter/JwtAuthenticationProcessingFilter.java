package kr.mybrary.userservice.global.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;

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
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String USER_SERVICE_URL = "/api/v1/users";
    private static final String LOGIN_URL = "/api/v1/auth/login";
    private static final String OAUTH2_URL = "/oauth2/authorization";
    private static final int REFRESH_TOKEN_EXPIRATION = 14;

    private final JwtService jwtService;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().contains(USER_SERVICE_URL) || request.getRequestURI().contains(LOGIN_URL) ||
                request.getRequestURI().contains(OAUTH2_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 요청 헤더에서 AccessToken 추출 - 없거나 유효하지 않으면 null
        String accessToken = jwtService.extractAccessToken(request).orElse(null);

        // TODO: 블랙리스트에서 accessToken확인 -> 있으면 액세스 거부
        if(accessToken != null && redisUtil.get(accessToken) != null) {
            System.out.println("로그아웃된 토큰이다.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String loginId = jwtService.getLoginId(accessToken).orElse(null);

        // 요청 헤더에서 RefreshToken 추출 - 없거나 유효하지 않으면 null
        String refreshToken = jwtService.extractRefreshToken(request).orElse(null);

        // RefreshToken이 존재하면 AccessToken 재발급 - 인증 처리는 하지 않음
        if (refreshToken != null) {
            checkRefreshToken(loginId, refreshToken);
            reIssueAccessTokenAndRefreshToken(response, accessToken);
            return;
        }

        // RefreshToken이 없거나 유효하지 않다면 AccessToken을 검사하고 인증 처리
        // AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
        // AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
        if (refreshToken == null) {
            saveAuthentication(getUserWithAccessToken(accessToken));
            request.setAttribute("USER-ID", loginId);
            filterChain.doFilter(request, response);
        }
    }

    private void checkRefreshToken(String loginId, String refreshToken) {
        String redisRefreshToken = (String) redisUtil.get(loginId);
        if(redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            throw new IllegalArgumentException("저장된 리프레쉬 토큰과 다름"); // TODO: 예외 처리
        }
    }

    // RefreshToken을 통해 DB에서 유저를 찾고, 새로운 RefreshToken을 발급하여 DB에 저장하고, AccessToken과 함께 응답
    private void reIssueAccessTokenAndRefreshToken(HttpServletResponse response, String loginId) {
        String reIssuedRefreshToken = jwtService.createRefreshToken(new Date());
        String reIssuedAccessToken = jwtService.createAccessToken(loginId, new Date());

        redisUtil.set(loginId, reIssuedRefreshToken, Duration.ofDays(REFRESH_TOKEN_EXPIRATION));
        jwtService.sendAccessAndRefreshToken(response, reIssuedAccessToken, reIssuedRefreshToken);
    }

     // AccessToken 검사 및 인증 처리
    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain, String accessToken)
            throws ServletException, IOException {
        log.info("checkAccessTokenAndAuthentication() 호출");
        // saveAuthentication(getUserWithAccessToken(accessToken));
        filterChain.doFilter(request, response); // TODO: 지워도 될까?
    }

    private User getUserWithAccessToken(String accessToken) {
        return userRepository.findByLoginId(jwtService.getLoginId(accessToken).orElseThrow(IllegalArgumentException::new)) // TODO: 예외 처리
                .orElseThrow(IllegalArgumentException::new); // TODO: 예외 처리
    }

    // 인증 객체 저장
    private void saveAuthentication(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getLoginId())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        // 인증 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));

        // SecurityContext에 인증 객체 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
