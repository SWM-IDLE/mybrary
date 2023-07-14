package kr.mybrary.userservice.user.domain.exception.follow;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class DuplicateFollowException extends ApplicationException {

        private final static int STATUS = 400;
        private final static String ERROR_CODE = "F-02";
        private final static String ERROR_MESSAGE = "이미 팔로우한 사용자입니다.";

        public DuplicateFollowException() {
            super(STATUS, ERROR_CODE, ERROR_MESSAGE);
        }
}
