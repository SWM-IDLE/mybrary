package kr.mybrary.userservice.user.domain.exception.user;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class UserNotSearchedException extends ApplicationException {

    private final static int STATUS = 404;
    private final static String ERROR_CODE = "U-07";
    private final static String ERROR_MESSAGE = "검색된 사용자가 없습니다.";

    public UserNotSearchedException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
