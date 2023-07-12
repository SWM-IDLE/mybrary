package kr.mybrary.userservice.user.domain.exception.profile;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class ProfileImageUrlNotFoundException extends ApplicationException {

    private final static int STATUS = 404;
    private final static String ERROR_CODE = "P-01";
    private final static String ERROR_MESSAGE = "프로필 이미지 URL이 존재하지 않습니다.";

    public ProfileImageUrlNotFoundException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
