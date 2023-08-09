package kr.mybrary.bookservice.review.presentation.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyReviewsOfBookGetResponse {

    private String title;
    private String isbn13;
    private int reviewCount;
    private double starRatingAverage;

    private List<ReviewElement> myBookReviewList;

    @Getter
    @Builder
    public static class ReviewElement {
        private Long id;
        private String userId;
        private String userNickname;
        private String userPictureUrl;
        private String content;
        private Double starRating;
        private String createdAt;
    }
}
