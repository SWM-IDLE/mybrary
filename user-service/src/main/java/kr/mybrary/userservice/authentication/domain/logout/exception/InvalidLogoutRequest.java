package kr.mybrary.userservice.authentication.domain.logout.exception;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class InvalidLogoutRequest extends ApplicationException {

    private final static int STATUS = 404;
    private final static String ERROR_CODE = "U-08";
    private final static String ERROR_MESSAGE = "로그아웃 요청이 잘못되었습니다.";

    public InvalidLogoutRequest() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
