package kr.mybrary.userservice.user.domain.exception.profile;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class ProfileImageSizeOptionNotSupportedException extends ApplicationException {

    private final static int STATUS = 404;
    private final static String ERROR_CODE = "P-04";
    private final static String ERROR_MESSAGE = "지원되지 않는 프로필 이미지 크기 옵션입니다. 현재 지원하는 옵션은 tiny, small, original 입니다.";

    public ProfileImageSizeOptionNotSupportedException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
