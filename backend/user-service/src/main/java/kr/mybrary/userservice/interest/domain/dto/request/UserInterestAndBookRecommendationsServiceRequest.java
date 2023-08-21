package kr.mybrary.userservice.interest.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInterestAndBookRecommendationsServiceRequest {

    private String loginId;
    private String type;
    private int page;

    public static UserInterestAndBookRecommendationsServiceRequest of(String loginId, String type, int page) {
        return UserInterestAndBookRecommendationsServiceRequest.builder()
                .loginId(loginId)
                .type(type)
                .page(page)
                .build();
    }
}
