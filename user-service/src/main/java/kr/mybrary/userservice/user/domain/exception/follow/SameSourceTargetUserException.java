package kr.mybrary.userservice.user.domain.exception.follow;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class SameSourceTargetUserException extends ApplicationException {

        private final static int STATUS = 400;
        private final static String ERROR_CODE = "F-01";
        private final static String ERROR_MESSAGE = "자기 자신을 팔로우 또는 언팔로우할 수 없습니다.";

        public SameSourceTargetUserException() {
            super(STATUS, ERROR_CODE, ERROR_MESSAGE);
        }
}
