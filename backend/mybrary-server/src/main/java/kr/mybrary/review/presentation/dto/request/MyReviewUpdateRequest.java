package kr.mybrary.review.presentation.dto.request;

import kr.mybrary.review.domain.dto.request.MyReviewUpdateServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyReviewUpdateRequest {

    private String content;
    private Double starRating;

    public MyReviewUpdateServiceRequest toServiceRequest(String userId, Long myReviewId) {
        return MyReviewUpdateServiceRequest.builder()
                .loginId(userId)
                .myReviewId(myReviewId)
                .content(content)
                .starRating(starRating)
                .build();
    }
}
