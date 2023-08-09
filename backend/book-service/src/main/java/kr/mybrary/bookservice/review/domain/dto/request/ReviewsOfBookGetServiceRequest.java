package kr.mybrary.bookservice.review.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewsOfBookGetServiceRequest {

    private String isbn13;

    public static ReviewsOfBookGetServiceRequest of(String isbn13) {
        return ReviewsOfBookGetServiceRequest.builder()
            .isbn13(isbn13)
            .build();
    }
}
