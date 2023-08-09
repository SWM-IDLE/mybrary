package kr.mybrary.bookservice.review.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyReviewsOfBookGetServiceRequest {

    private String isbn13;

    public static MyReviewsOfBookGetServiceRequest of(String isbn13) {
        return MyReviewsOfBookGetServiceRequest.builder()
            .isbn13(isbn13)
            .build();
    }
}
