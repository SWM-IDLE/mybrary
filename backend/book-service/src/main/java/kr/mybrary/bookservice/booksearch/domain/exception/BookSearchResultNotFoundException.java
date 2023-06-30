package kr.mybrary.bookservice.booksearch.domain.exception;

import kr.mybrary.bookservice.global.exception.ApplicationException;

public class BookSearchResultNotFoundException extends ApplicationException {

    private final static int STATUS = 404;
    private final static String ERROR_CODE = "B-01";
    private final static String ERROR_MESSAGE = "도서 검색 결과가 존재하지 않습니다.";

    public BookSearchResultNotFoundException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
