package kr.mybrary.userservice.user.domain.exception.user;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class EmailAlreadyRegisteredException extends ApplicationException {

    private final static int STATUS = 400;
    private final static String ERROR_CODE = "U-06";
    private final static String ERROR_MESSAGE = "이미 등록된 이메일은 수정할 수 없습니다.";

    public EmailAlreadyRegisteredException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
