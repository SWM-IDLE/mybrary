package kr.mybrary.userservice.user.domain.exception.profile;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class ProfileUpdateRequestNotAuthenticated extends ApplicationException {

    private final static int STATUS = 403;
    private final static String ERROR_CODE = "P-03";
    private final static String ERROR_MESSAGE = "프로필을 수정할 수 있는 권한이 없습니다. 로그인한 사용자와 프로필 수정을 요청한 사용자가 일치하지 않습니다.";

    public ProfileUpdateRequestNotAuthenticated() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
