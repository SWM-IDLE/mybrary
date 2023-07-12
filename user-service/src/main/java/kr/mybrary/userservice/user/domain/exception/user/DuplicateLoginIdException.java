package kr.mybrary.userservice.user.domain.exception.user;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class DuplicateLoginIdException extends ApplicationException {

    private final static int STATUS = 400;
    private final static String ERROR_CODE = "U-02";
    private final static String ERROR_MESSAGE = "이미 존재하는 로그인 아이디입니다.";

    public DuplicateLoginIdException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }


}
