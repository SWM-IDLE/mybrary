package kr.mybrary.bookservice.book.domain.exception;

import kr.mybrary.bookservice.global.exception.ApplicationException;

public class BookNotFoundException extends ApplicationException {

    private final static int STATUS = 404;
    private final static String ERROR_CODE = "B-05";
    private final static String ERROR_MESSAGE = "해당 도서가 DB에 존재하지 않습니다.";

    public BookNotFoundException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
