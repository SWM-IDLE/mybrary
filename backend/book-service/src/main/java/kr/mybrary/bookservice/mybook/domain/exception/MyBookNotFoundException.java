package kr.mybrary.bookservice.mybook.domain.exception;

import kr.mybrary.bookservice.global.exception.ApplicationException;

public class MyBookNotFoundException extends ApplicationException {

    private final static int STATUS = 404;
    private final static String ERROR_CODE = "B-03";
    private final static String ERROR_MESSAGE = "마이북으로 등록된 도서가 아닙니다.";

    public MyBookNotFoundException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
