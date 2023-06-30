package kr.mybrary.userservice.authentication.domain.oauth2.handler;

import static kr.mybrary.userservice.authentication.domain.oauth2.OAuth2Exception.SOCIAL_LOGIN_FAILED;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    static final String ENCODING_UTF_8 = "UTF-8";
    static final String CONTENT_TYPE_JSON = "application/json";

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        log.info("OAuth2 Login Failure Handler 실행 - OAuth2 로그인 실패 : " + exception.getMessage());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding(ENCODING_UTF_8);
        response.setContentType(CONTENT_TYPE_JSON);
        response.getWriter().write(generateResponseBody(exception));
    }

    private String generateResponseBody(AuthenticationException exception)
            throws JsonProcessingException {
        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("errorCode", getErrorCode(exception));
        responseMessage.put("errorMessage", getErrorMessage(exception));
        return objectMapper.writeValueAsString(responseMessage);
    }

    private String getErrorCode(AuthenticationException exception) {
        if(exception instanceof OAuth2AuthenticationException) {
            return SOCIAL_LOGIN_FAILED.getErrorCode();
        }
        return null;
    }

    private String getErrorMessage(AuthenticationException exception) {
        if(exception instanceof OAuth2AuthenticationException) {
            return String.format(SOCIAL_LOGIN_FAILED.getErrorMessage(), exception.getMessage());
        }
        return exception.getMessage();
    }


}
