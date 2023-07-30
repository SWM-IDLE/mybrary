package kr.mybrary.userservice.authentication.domain.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final String AUTHORIZATION_EXCEPTION_MESSAGE = "인증되지 않은 사용자입니다.";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        log.info("AuthenticationExceptionType: " + authException.getClass().getSimpleName() + " , message: " + authException.getMessage());

        final Map<String, Object> body = new HashMap<>();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 응답 객체 초기화
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", AUTHORIZATION_EXCEPTION_MESSAGE);
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());
        // response 객체에 응답 객체를 넣어줌
        objectMapper.writeValue(response.getOutputStream(), body);
        log.info("response status: " + response.getStatus());

    }

}
