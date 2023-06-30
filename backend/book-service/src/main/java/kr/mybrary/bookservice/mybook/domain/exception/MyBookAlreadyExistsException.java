package kr.mybrary.bookservice.mybook.domain.exception;

import kr.mybrary.bookservice.global.exception.ApplicationException;

public class MyBookAlreadyExistsException extends ApplicationException {

    private final static int STATUS = 400;
    private final static String ERROR_CODE = "B-02";
    private final static String ERROR_MESSAGE = "이미 마이북으로 등록된 도서입니다.";

    public MyBookAlreadyExistsException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
