package kr.mybrary.bookservice.review.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyReviewUpdateServiceRequest {

    private String loginId;
    private Long myReviewId;
    private String content;
    private Double starRating;

    public static MyReviewUpdateServiceRequest of(String loginId, Long myReviewId, String content, Double starRating) {
        return MyReviewUpdateServiceRequest.builder()
                .loginId(loginId)
                .myReviewId(myReviewId)
                .content(content)
                .starRating(starRating)
                .build();
    }

}
