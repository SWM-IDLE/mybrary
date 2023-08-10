package kr.mybrary.userservice.user.domain.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowingServiceResponse {

    private String userId;
    private List<FollowResponse> followings;

}
