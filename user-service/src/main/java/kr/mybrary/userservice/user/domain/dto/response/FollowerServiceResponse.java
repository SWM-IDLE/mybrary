package kr.mybrary.userservice.user.domain.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowerServiceResponse {

    private String requestLoginId;
    private List<FollowResponse> followers;

}
