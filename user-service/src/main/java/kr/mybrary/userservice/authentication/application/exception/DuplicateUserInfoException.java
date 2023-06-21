package kr.mybrary.userservice.authentication.application.exception;

import kr.mybrary.userservice.global.exception.CustomException;

public class DuplicateUserInfoException extends CustomException {

    private final static String ERROR_CODE = "U-01";
    private final static String ERROR_MESSAGE = "이미 존재하는 회원입니다.";

    public DuplicateUserInfoException() {
        super(ERROR_CODE, ERROR_MESSAGE);
    }
}
