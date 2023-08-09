package kr.mybrary.bookservice.review.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyReviewOfMyBookGetServiceRequest {

    private Long myBookId;

    public static MyReviewOfMyBookGetServiceRequest of(Long myBookId) {
        return MyReviewOfMyBookGetServiceRequest.builder()
                .myBookId(myBookId)
                .build();
    }
}
