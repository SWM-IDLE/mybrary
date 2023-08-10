package kr.mybrary.userservice.user.domain.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowerServiceResponse {

    private String userId;
    private List<FollowResponse> followers;

}
