package kr.mybrary.userservice.authentication.domain.exception;

import kr.mybrary.userservice.global.exception.CustomException;

public class DuplicateNicknameException extends CustomException {

    private final static String ERROR_CODE = "U-03";
    private final static String ERROR_MESSAGE = "이미 존재하는 닉네임입니다.";

    public DuplicateNicknameException() {
        super(ERROR_CODE, ERROR_MESSAGE);
    }

}
