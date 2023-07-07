package kr.mybrary.userservice.user.domain.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowingServiceResponse {

    private String requestLoginId;
    private List<FollowResponse> followings;

}
