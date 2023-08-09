package kr.mybrary.bookservice.review.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyReviewOfMyBookGetResponse {

    private Long id;
    private String content;
    private Double starRating;
    private String createdAt;
    private String updatedAt;
}
