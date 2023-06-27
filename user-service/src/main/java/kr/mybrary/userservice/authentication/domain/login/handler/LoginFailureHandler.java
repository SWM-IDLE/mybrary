package kr.mybrary.userservice.authentication.domain.login.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain:charset=utf-8");
        if (exception.getMessage().equals("Bad credentials")) {
            response.getWriter().write("비밀번호가 일치하지 않습니다.");
            return;
        }
        response.getWriter().write(exception.getMessage());
        log.info("로그인에 실패하였습니다. 실패 이유 : {}", exception.getMessage());
    }

}
