package kr.mybrary.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.mybrary.authentication.domain.login.CustomAuthenticationEntryPoint;
import kr.mybrary.authentication.domain.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import kr.mybrary.authentication.domain.login.handler.LoginFailureHandler;
import kr.mybrary.authentication.domain.login.handler.LoginSuccessHandler;
import kr.mybrary.authentication.domain.logout.filter.LogoutExceptionFilter;
import kr.mybrary.authentication.domain.logout.handler.CustomLogoutHandler;
import kr.mybrary.authentication.domain.oauth2.handler.OAuth2LoginFailureHandler;
import kr.mybrary.authentication.domain.oauth2.handler.OAuth2LoginSuccessHandler;
import kr.mybrary.authentication.domain.oauth2.service.CustomOAuth2UserService;
import kr.mybrary.global.jwt.JwtAuthenticationFilter;
import kr.mybrary.global.util.JwtUtil;
import kr.mybrary.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final RedisUtil redisUtil;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    /**
     * /web/** 경로: 세션 기반 폼 로그인 (Thymeleaf MPA 용)
     * JWT 필터를 적용하지 않고 Spring Security 기본 세션 인증을 사용한다.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/web/**")
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(form -> form
                        .loginPage("/web/login")
                        .loginProcessingUrl("/web/login")
                        .defaultSuccessUrl("/web", true)
                        .failureUrl("/web/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/web/logout")
                        .logoutSuccessUrl("/web/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/web/login", "/web/signup", "/web/find-password").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    /**
     * /api/v1/**, /auth/v1/** 경로: 기존 JWT 기반 인증 (REST API 용)
     */
    @Bean
    @Order(2)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper)))
                .authorizeHttpRequests(request -> request
                        // 회원가입 (JWT 불필요)
                        .requestMatchers("/api/v1/users/sign-up").permitAll()
                        // 로그인 (CustomJsonUsernamePasswordAuthenticationFilter 에서 처리)
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        // 토큰 갱신 (Refresh Token으로 처리, JWT Access Token 불필요)
                        .requestMatchers("/auth/v1/refresh").permitAll()
                        // 소셜 로그인 시작 및 콜백
                        .requestMatchers("/oauth2/authorization/**", "/login/oauth2/code/**").permitAll()
                        // 로그아웃 (CustomLogoutHandler 에서 처리)
                        .requestMatchers("/api/v1/auth/logout").permitAll()
                        // 헬스체크 및 API 문서
                        .requestMatchers("/actuator/**", "/docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(customLogoutHandler())
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                );

        // JwtAuthenticationFilter -> LogoutExceptionFilter -> LogoutFilter -> AbstractAuthenticationProcessingFilter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(logoutExceptionFilter(), LogoutFilter.class);
        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        provider.setHideUserNotFoundExceptions(false);
        return new ProviderManager(provider);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtUtil, redisUtil);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public CustomLogoutHandler customLogoutHandler() {
        return new CustomLogoutHandler(jwtUtil, redisUtil);
    }

    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, redisUtil, userDetailsService, objectMapper);
    }

    @Bean
    public LogoutExceptionFilter logoutExceptionFilter() {
        LogoutExceptionFilter logoutExceptionFilter = new LogoutExceptionFilter(objectMapper);
        return logoutExceptionFilter;
    }
}
