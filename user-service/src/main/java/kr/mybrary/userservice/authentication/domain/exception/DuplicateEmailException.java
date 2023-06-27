package kr.mybrary.userservice.authentication.domain.exception;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class DuplicateEmailException extends ApplicationException {

    private final static String ERROR_CODE = "U-04";
    private final static String ERROR_MESSAGE = "이미 존재하는 이메일입니다.";

    public DuplicateEmailException() {
        super(ERROR_CODE, ERROR_MESSAGE);
    }

}
