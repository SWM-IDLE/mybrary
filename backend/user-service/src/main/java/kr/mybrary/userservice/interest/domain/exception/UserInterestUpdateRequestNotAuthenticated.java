package kr.mybrary.userservice.interest.domain.exception;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class UserInterestUpdateRequestNotAuthenticated extends ApplicationException {

    private final static int STATUS = 403;
    private final static String ERROR_CODE = "I-04";
    private final static String ERROR_MESSAGE = "관심사를 수정할 수 있는 권한이 없습니다. 로그인한 사용자와 관심사 수정을 요청한 사용자가 일치하지 않습니다.";

    public UserInterestUpdateRequestNotAuthenticated() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
