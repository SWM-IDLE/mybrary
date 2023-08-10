package kr.mybrary.bookservice.review.presentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
public class MyReviewCreateRequest {

    @Length(max = 500, message = "리뷰 내용은 500자 이하로 작성해주세요.")
    private String content;

    @Min(value = 0, message = "별점은 0점 이상으로 작성해주세요.")
    @Max(value = 5, message = "별점은 5점 이하로 작성해주세요.")
    @NotBlank(message = "별점은 필수입니다.")
    private Double starRating;

    public MyReviewCreateServiceRequest toServiceRequest(String userId, Long myBookId) {
        return MyReviewCreateServiceRequest.builder()
                .loginId(userId)
                .myBookId(myBookId)
                .content(content)
                .starRating(starRating)
                .build();
    }
}
