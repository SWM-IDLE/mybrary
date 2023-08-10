package kr.mybrary.bookservice.review.domain.exception;

import kr.mybrary.bookservice.global.exception.ApplicationException;

public class MyReviewAccessDeniedException extends ApplicationException {

    private final static int STATUS = 403;
    private final static String ERROR_CODE = "B-08";
    private final static String ERROR_MESSAGE = "해당 마이 리뷰에 대해 접근할 수 권한이 없습니다.";

    public MyReviewAccessDeniedException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
