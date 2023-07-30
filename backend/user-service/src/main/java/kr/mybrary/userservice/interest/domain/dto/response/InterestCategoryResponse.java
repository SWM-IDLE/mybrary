package kr.mybrary.userservice.interest.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class InterestCategoryResponse {

    private Long id;
    private String name;
    private String description;
    private List<InterestResponse> interestResponses;

}
