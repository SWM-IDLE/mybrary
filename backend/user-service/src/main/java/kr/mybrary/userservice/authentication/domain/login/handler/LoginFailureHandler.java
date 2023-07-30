package kr.mybrary.userservice.authentication.domain.login.handler;

import static kr.mybrary.userservice.authentication.domain.login.LoginException.CONTENT_TYPE_NOT_JSON;
import static kr.mybrary.userservice.authentication.domain.login.LoginException.LOGIN_ID_NOT_FOUND;
import static kr.mybrary.userservice.authentication.domain.login.LoginException.PASSWORD_NOT_MATCH;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    static final String ENCODING_UTF_8 = "UTF-8";
    static final String CONTENT_TYPE_JSON = "application/json";

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding(ENCODING_UTF_8);
        response.setContentType(CONTENT_TYPE_JSON);
        response.getWriter().write(generateRequestBody(exception));
        log.info("로그인에 실패하였습니다. 실패 이유 : {}", exception.getMessage());
    }

    private String generateRequestBody(AuthenticationException exception)
            throws JsonProcessingException {
        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("errorCode", getErrorCode(exception));
        responseMessage.put("errorMessage", getErrorMessage(exception));
        return objectMapper.writeValueAsString(responseMessage);
    }

    private String getErrorCode(AuthenticationException exception) {
        if (exception instanceof AuthenticationServiceException) {
            return CONTENT_TYPE_NOT_JSON.getErrorCode();
        }
        if (exception instanceof UsernameNotFoundException) {
            return LOGIN_ID_NOT_FOUND.getErrorCode();
        }
        if (exception instanceof BadCredentialsException) {
            return PASSWORD_NOT_MATCH.getErrorCode();
        }
        return null;
    }

    private String getErrorMessage(AuthenticationException exception) {
        if (exception instanceof AuthenticationServiceException) {
            return String.format(CONTENT_TYPE_NOT_JSON.getErrorMessage(), exception.getMessage());
        }
        if (exception instanceof UsernameNotFoundException) {
            return String.format(LOGIN_ID_NOT_FOUND.getErrorMessage(), exception.getMessage());
        }
        if (exception instanceof BadCredentialsException) {
            return PASSWORD_NOT_MATCH.getErrorMessage();
        }
        return exception.getMessage();
    }

}
