package kr.mybrary.bookservice.review.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyReviewDeleteServiceRequest {

    private Long myReviewId;
    private String loginId;

    public static MyReviewDeleteServiceRequest of(Long myReviewId, String loginId) {
        return MyReviewDeleteServiceRequest.builder()
                .myReviewId(myReviewId)
                .loginId(loginId)
                .build();
    }

}
