package kr.mybrary.bookservice.mybook.domain.exception;

import kr.mybrary.bookservice.global.exception.ApplicationException;

public class MyBookAccessDeniedException extends ApplicationException {

    private final static int STATUS = 403;
    private final static String ERROR_CODE = "B-04";
    private final static String ERROR_MESSAGE = "마이북에 대해 접근할 수 없습니다.";

    public MyBookAccessDeniedException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
