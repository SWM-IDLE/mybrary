package kr.mybrary.userservice.interest.domain.exception;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class UserInterestUpdateRequestSizeExceededException extends ApplicationException {

    private final static int STATUS = 400;
    private final static String ERROR_CODE = "I-02";
    private final static String ERROR_MESSAGE = "관심사는 최대 3개까지 설정할 수 있습니다.";

    public UserInterestUpdateRequestSizeExceededException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
