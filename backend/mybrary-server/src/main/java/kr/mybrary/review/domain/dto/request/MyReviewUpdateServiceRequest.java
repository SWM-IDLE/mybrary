package kr.mybrary.review.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyReviewUpdateServiceRequest {

    private String loginId;
    private Long myReviewId;
    private String content;
    private Double starRating;

}
