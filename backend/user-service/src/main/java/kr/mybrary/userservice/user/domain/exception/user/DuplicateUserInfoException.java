package kr.mybrary.userservice.user.domain.exception.user;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class DuplicateUserInfoException extends ApplicationException {

    private final static int STATUS = 400;
    private final static String ERROR_CODE = "U-01";
    private final static String ERROR_MESSAGE = "이미 존재하는 회원입니다.";

    public DuplicateUserInfoException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
