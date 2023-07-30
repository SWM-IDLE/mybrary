package kr.mybrary.userservice.authentication.domain.login.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

/*
    /login 으로 다음과 같이 POST 요청이 왔을 때 실행되는 필터
    {
        "loginId": "test",
        "password": "test"
    }
 */
public class CustomJsonUsernamePasswordAuthenticationFilter extends
        AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/api/v1/auth/login";
    private static final String HTTP_METHOD_POST = "POST";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String LOGIN_ID_KEY = "loginId";
    private static final String PASSWORD_KEY = "password";
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD_POST);
    private final ObjectMapper objectMapper;

    public CustomJsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response)
            throws AuthenticationException, IOException {
        if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE_JSON)) {
            throw new AuthenticationServiceException(request.getContentType());
        }

        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        Map<String, String> usernameAndPassword = objectMapper.readValue(messageBody, Map.class);
        String loginId = usernameAndPassword.get(LOGIN_ID_KEY);
        String password = usernameAndPassword.get(PASSWORD_KEY);

        // UsernamePasswordAuthenticationToken의 파라미터 principal, credentials에 대입
        UsernamePasswordAuthenticationToken authRequset = new UsernamePasswordAuthenticationToken(
                loginId, password);

        // AuthenticationManager에게 인증 처리 요청
        return this.getAuthenticationManager().authenticate(authRequset);
    }

}
