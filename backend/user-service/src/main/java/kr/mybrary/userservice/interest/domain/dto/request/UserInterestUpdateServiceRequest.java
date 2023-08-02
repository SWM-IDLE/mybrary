package kr.mybrary.userservice.interest.domain.dto.request;

import kr.mybrary.userservice.interest.presentation.dto.request.UserInterestUpdateRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserInterestUpdateServiceRequest {

    private String loginId;
    private List<Long> interestIds;

    public static UserInterestUpdateServiceRequest of(String loginId, List<UserInterestUpdateRequest.InterestRequest> interestRequests) {
        return UserInterestUpdateServiceRequest.builder()
                .loginId(loginId)
                .interestIds(interestRequests.stream().map(UserInterestUpdateRequest.InterestRequest::getId).toList())
                .build();
    }

}
