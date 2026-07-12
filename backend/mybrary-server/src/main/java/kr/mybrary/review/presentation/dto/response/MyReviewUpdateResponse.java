package kr.mybrary.review.presentation.dto.response;

import kr.mybrary.review.persistence.MyReview;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyReviewUpdateResponse {

    private Long id;
    private String content;
    private Double starRating;

    public static MyReviewUpdateResponse of(MyReview myReview) {
        return MyReviewUpdateResponse.builder()
                .id(myReview.getId())
                .content(myReview.getContent())
                .starRating(myReview.getStarRating())
                .build();
    }
}
