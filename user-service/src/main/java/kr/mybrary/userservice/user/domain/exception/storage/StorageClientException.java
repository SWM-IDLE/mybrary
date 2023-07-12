package kr.mybrary.userservice.user.domain.exception.storage;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class StorageClientException extends ApplicationException {

        private final static int STATUS = 500;
        private final static String ERROR_CODE = "S-01";
        private final static String ERROR_MESSAGE = "스토리지 서버와 통신에 실패했습니다.";

        public StorageClientException() {
            super(STATUS, ERROR_CODE, ERROR_MESSAGE);
        }

}
