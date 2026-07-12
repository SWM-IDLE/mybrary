package kr.mybrary.user.domain.exception.user;

import kr.mybrary.global.exception.ApplicationException;

public class DuplicateNicknameException extends ApplicationException {

    private final static int STATUS = 400;
    private final static String ERROR_CODE = "U-03";
    private final static String ERROR_MESSAGE = "이미 존재하는 닉네임입니다.";

    public DuplicateNicknameException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
