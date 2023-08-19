package kr.mybrary.userservice.authentication.domain.exception;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class AccessTokenNotFoundException extends ApplicationException {

    private final static int STATUS = 401;
    private final static String ERROR_CODE = "A-01";
    private final static String ERROR_MESSAGE = "액세스 토큰이 존재하지 않습니다.";

    public AccessTokenNotFoundException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
