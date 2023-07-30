package kr.mybrary.userservice.interest.domain.exception;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class UserInterestNotFoundException extends ApplicationException {

    private final static int STATUS = 404;
    private final static String ERROR_CODE = "I-01";
    private final static String ERROR_MESSAGE = "등록된 관심사가 없습니다.";

    public UserInterestNotFoundException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
