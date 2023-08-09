package kr.mybrary.bookservice.review.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewOfMyBookGetServiceRequest {

    private Long myBookId;

    public static ReviewOfMyBookGetServiceRequest of(Long myBookId) {
        return ReviewOfMyBookGetServiceRequest.builder()
                .myBookId(myBookId)
                .build();
    }
}
