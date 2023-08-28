package kr.mybrary.bookservice.booksearch.domain.exception;

import kr.mybrary.bookservice.global.exception.ApplicationException;

public class AladinApiUnavailableException extends ApplicationException {

    private final static int STATUS = 404;
    private final static String ERROR_CODE = "B-11";
    private final static String ERROR_MESSAGE = "알라딘 API 서버가 응답하지 않습니다.";

    public AladinApiUnavailableException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
