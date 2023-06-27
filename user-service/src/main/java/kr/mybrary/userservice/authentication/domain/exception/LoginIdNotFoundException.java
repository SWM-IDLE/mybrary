package kr.mybrary.userservice.authentication.domain.exception;

import org.springframework.security.core.AuthenticationException;

public class LoginIdNotFoundException extends AuthenticationException {

    private final static String ERROR_MESSAGE = "존재하지 않는 로그인 아이디입니다.";

    public LoginIdNotFoundException() {
        super(ERROR_MESSAGE);
    }

}
