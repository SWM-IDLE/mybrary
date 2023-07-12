package kr.mybrary.userservice.user.domain.exception.profile;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class ProfileImageFileSizeExceededException extends ApplicationException {

        private final static int STATUS = 400;
        private final static String ERROR_CODE = "P-02";
        private final static String ERROR_MESSAGE = "프로필 이미지 파일의 크기가 너무 큽니다. 최대 5MB까지 업로드 가능합니다.";

        public ProfileImageFileSizeExceededException() {
            super(STATUS, ERROR_CODE, ERROR_MESSAGE);
        }

}
