package kr.mybrary.bookservice.booksearch.domain.exception;

import kr.mybrary.bookservice.global.exception.ApplicationException;

public class UnsupportedSearchAPIException extends ApplicationException {

    private final static int STATUS = 404;
    private final static String ERROR_CODE = "B-10";
    private final static String ERROR_MESSAGE = "지원되지 않는 도서 검색 API 입니다.";

    public UnsupportedSearchAPIException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
