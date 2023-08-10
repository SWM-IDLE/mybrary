package kr.mybrary.userservice.interest.domain.dto.request;

import kr.mybrary.userservice.interest.presentation.dto.request.UserInterestUpdateRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserInterestUpdateServiceRequest {

    private String userId;
    private String loginId;
    private List<Long> interestIds;

    public static UserInterestUpdateServiceRequest of(String userId, String loginId,
                                                      List<UserInterestUpdateRequest.InterestRequest> interestRequests) {
        return UserInterestUpdateServiceRequest.builder()
                .userId(userId)
                .loginId(loginId)
                .interestIds(interestRequests.stream().map(UserInterestUpdateRequest.InterestRequest::getId).toList())
                .build();
    }

}
