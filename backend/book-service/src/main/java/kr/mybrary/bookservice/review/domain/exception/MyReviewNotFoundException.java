package kr.mybrary.bookservice.review.domain.exception;

import kr.mybrary.bookservice.global.exception.ApplicationException;

public class MyReviewNotFoundException extends ApplicationException {

    private final static int STATUS = 404;
    private final static String ERROR_CODE = "B-09";
    private final static String ERROR_MESSAGE = "마이 리뷰를 찾을 수 없습니다.";

    public MyReviewNotFoundException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
