package kr.mybrary.bookservice.review;

import kr.mybrary.bookservice.review.domain.dto.request.MyBookReviewCreateServiceRequest;
import kr.mybrary.bookservice.review.presentation.dto.request.MyBookReviewCreateRequest;

public class MyBookReviewDtoTestData {

    public static MyBookReviewCreateServiceRequest createMyBookReviewCreateServiceRequest() {
        return MyBookReviewCreateServiceRequest.builder()
                .myBookId(1L)
                .loginId("LOGIN_USER_ID")
                .content("리뷰 내용입니다.")
                .starRating(4.5)
                .build();
    }

    public static MyBookReviewCreateRequest createMyBookReviewCreateRequest() {
        return MyBookReviewCreateRequest.builder()
                .content("리뷰 내용입니다.")
                .starRating(4.5)
                .build();
    }
}
