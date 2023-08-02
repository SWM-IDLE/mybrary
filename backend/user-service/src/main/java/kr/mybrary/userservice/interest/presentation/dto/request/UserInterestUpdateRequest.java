package kr.mybrary.userservice.interest.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInterestUpdateRequest {

    private List<InterestRequest> interestRequests;

    @Getter
    @Builder
    public static class InterestRequest {
        private Long id;
        private String name;
    }

}
