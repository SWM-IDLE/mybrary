package kr.mybrary.userservice.interest.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserInterestServiceResponse {

    private String userId;
    private List<InterestResponse> userInterests;

}
