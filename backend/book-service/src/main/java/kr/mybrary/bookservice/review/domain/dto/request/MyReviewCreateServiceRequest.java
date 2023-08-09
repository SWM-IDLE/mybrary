package kr.mybrary.bookservice.review.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyReviewCreateServiceRequest {

    private Long myBookId;
    private String loginId;
    private String content;
    private Double starRating;

}
