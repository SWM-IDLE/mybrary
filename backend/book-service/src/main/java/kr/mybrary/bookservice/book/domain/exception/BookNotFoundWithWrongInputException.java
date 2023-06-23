package kr.mybrary.bookservice.book.domain.exception;

import kr.mybrary.bookservice.global.exception.ApplicationException;

public class BookNotFoundWithWrongInputException extends ApplicationException {

    private final static String ERROR_CODE = "B-01";
    private final static String ERROR_MESSAGE = "잘못된 검색으로 찾을 수 없는 도서입니다.";

    public BookNotFoundWithWrongInputException() {
        super(ERROR_CODE, ERROR_MESSAGE);
    }
}
