package kr.mybrary.bookservice.book.domain.exception;

import kr.mybrary.bookservice.global.exception.ApplicationException;

public class BookSearchResultNotFoundException extends ApplicationException {

    private final static int STATUS = 404;
    private final static String ERROR_CODE = "B-01";
    private final static String ERROR_MESSAGE = "잘못된 검색으로 찾을 수 없는 도서입니다.";

    public BookSearchResultNotFoundException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
