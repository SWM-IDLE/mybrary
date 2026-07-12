package kr.mybrary.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequestWrapper;
import kr.mybrary.global.util.JwtUtil;
import kr.mybrary.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    private static final List<String> WHITELIST = List.of(
            "/sign-up", "/auth", "/oauth2/authorization", "/login"
    );

    private static final String USER_ID_HEADER = "USER-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // 1. 화이트리스트 경로는 그냥 통과
        if (isWhitelisted(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Access Token 추출
        Optional<String> tokenOpt = jwtUtil.extractAccessToken(request);

        // 3. 토큰이 없으면 인증 없이 통과 (Security 설정에서 경로별 권한 제어)
        if (tokenOpt.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        String token = tokenOpt.get();

        // 4a. /refresh 경로이면 Refresh Token 처리이므로 Access Token 검증 생략
        if (requestURI.contains("/refresh")) {
            chain.doFilter(request, response);
            return;
        }

        // 4b. Redis 블랙리스트 확인 (로그아웃된 토큰)
        if (redisUtil.hasKey(token)) {
            log.warn("로그아웃된 Access Token 사용 시도. URI: {}", requestURI);
            writeUnauthorizedResponse(response, "AU-01", "이미 로그아웃된 토큰입니다.");
            return;
        }

        // 4c~d. JWT 검증 및 loginId 추출 후 SecurityContext 설정
        try {
            Optional<String> loginIdOpt = jwtUtil.getLoginIdFromValidAccessToken(token);

            if (loginIdOpt.isEmpty()) {
                log.warn("Access Token에서 loginId를 추출할 수 없습니다. URI: {}", requestURI);
                writeUnauthorizedResponse(response, "AU-02", "유효하지 않은 토큰입니다.");
                return;
            }

            String loginId = loginIdOpt.get();

            // UserDetails 로드 후 SecurityContext 인증 설정
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // USER-ID 헤더를 주입한 래핑된 request로 필터 체인 계속 진행
            HttpServletRequest wrappedRequest = new UserIdInjectingRequest(request, loginId);
            chain.doFilter(wrappedRequest, response);

        } catch (JwtException e) {
            log.warn("JWT 검증 실패. URI: {}, 사유: {}", requestURI, e.getMessage());
            writeUnauthorizedResponse(response, "AU-03", "유효하지 않은 토큰입니다.");
        }
    }

    private boolean isWhitelisted(String requestURI) {
        return WHITELIST.stream().anyMatch(requestURI::startsWith);
    }

    private void writeUnauthorizedResponse(HttpServletResponse response,
                                           String errorCode,
                                           String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(),
                Map.of("errorCode", errorCode, "errorMessage", errorMessage));
    }

    /**
     * USER-ID 헤더를 동적으로 주입하는 HttpServletRequestWrapper.
     * 기존 헤더는 모두 유지하면서 USER-ID 헤더만 추가/덮어씁니다.
     */
    private static class UserIdInjectingRequest extends HttpServletRequestWrapper {

        private final String loginId;

        public UserIdInjectingRequest(HttpServletRequest request, String loginId) {
            super(request);
            this.loginId = loginId;
        }

        @Override
        public String getHeader(String name) {
            if (USER_ID_HEADER.equalsIgnoreCase(name)) {
                return loginId;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if (USER_ID_HEADER.equalsIgnoreCase(name)) {
                return Collections.enumeration(List.of(loginId));
            }
            return super.getHeaders(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> names = Collections.list(super.getHeaderNames());
            if (!names.contains(USER_ID_HEADER)) {
                names.add(USER_ID_HEADER);
            }
            return Collections.enumeration(names);
        }
    }
}
