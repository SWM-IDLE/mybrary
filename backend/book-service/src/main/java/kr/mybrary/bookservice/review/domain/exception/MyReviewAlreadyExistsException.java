package kr.mybrary.bookservice.review.domain.exception;

import kr.mybrary.bookservice.global.exception.ApplicationException;

public class MyReviewAlreadyExistsException extends ApplicationException {

    private final static int STATUS = 400;
    private final static String ERROR_CODE = "B-07";
    private final static String ERROR_MESSAGE = "해당 마이북에 이미 리뷰가 존재합니다.";

    public MyReviewAlreadyExistsException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
