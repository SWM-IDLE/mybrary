package kr.mybrary.bookservice.book.domain.exception;

import kr.mybrary.bookservice.global.exception.ApplicationException;

public class BookAlreadyExistsException extends ApplicationException {

    private final static int STATUS = 400;
    private final static String ERROR_CODE = "B-06";
    private final static String ERROR_MESSAGE = "이미 등록된 도서입니다.";

    public BookAlreadyExistsException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}