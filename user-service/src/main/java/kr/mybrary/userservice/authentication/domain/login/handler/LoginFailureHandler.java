package kr.mybrary.userservice.authentication.domain.login.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.print.attribute.standard.Media;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    static final String ENCODING_UTF_8 = "UTF-8";
    static final String CONTENT_TYPE_JSON = "application/json";
    static final String ERROR_CODE_LOGIN_ID_NOT_FOUND = "U-05";
    static final String ERROR_CODE_PASSWORD_INCORRECT = "U-06";
    static final String ERROR_CODE_CONTENT_TYPE_NOT_JSON = "U-07";
    static final String ERROR_MESSAGE_LOGIN_ID_NOT_FOUND = "존재하지 않는 아이디입니다: %s";
    static final String ERROR_MESSAGE_PASSWORD_INCORRECT = "비밀번호가 일치하지 않습니다.";
    static final String ERROR_MESSAGE_CONTENT_TYPE_NOT_JSON = "지원되지 않는 Content-Type 입니다: %s. JSON 형식으로 요청해주세요.";

    private static final ObjectMapper objectMapper = new ObjectMapper();

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
            return ERROR_CODE_CONTENT_TYPE_NOT_JSON;
        }
        if (exception instanceof UsernameNotFoundException) {
            return ERROR_CODE_LOGIN_ID_NOT_FOUND;
        }
        if (exception instanceof BadCredentialsException) {
            return ERROR_CODE_PASSWORD_INCORRECT;
        }
        return null;
    }

    private String getErrorMessage(AuthenticationException exception) {
        if (exception instanceof AuthenticationServiceException) {
            return String.format(ERROR_MESSAGE_CONTENT_TYPE_NOT_JSON, exception.getMessage());
        }
        if (exception instanceof UsernameNotFoundException) {
            return String.format(ERROR_MESSAGE_LOGIN_ID_NOT_FOUND, exception.getMessage());
        }
        if (exception instanceof BadCredentialsException) {
            return ERROR_MESSAGE_PASSWORD_INCORRECT;
        }
        return exception.getMessage();
    }

}
