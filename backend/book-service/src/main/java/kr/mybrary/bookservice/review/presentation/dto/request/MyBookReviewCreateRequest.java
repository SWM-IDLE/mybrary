package kr.mybrary.bookservice.review.presentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import kr.mybrary.bookservice.review.domain.dto.request.MyBookReviewCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookReviewCreateRequest {

    @Max(value = 50, message = "리뷰 내용은 50자 이하로 작성해주세요.")
    @NotBlank(message = "리뷰 내용은 필수입니다.")
    private String content;

    @Min(value = 0, message = "별점은 0점 이상으로 작성해주세요.")
    @Max(value = 5, message = "별점은 5점 이하로 작성해주세요.")
    @NotBlank(message = "별점은 필수입니다.")
    private Double starRating;

    public MyBookReviewCreateServiceRequest toServiceRequest(String userId, Long myBookId) {
        return MyBookReviewCreateServiceRequest.builder()
                .loginId(userId)
                .myBookId(myBookId)
                .content(content)
                .starRating(starRating)
                .build();
    }
}
