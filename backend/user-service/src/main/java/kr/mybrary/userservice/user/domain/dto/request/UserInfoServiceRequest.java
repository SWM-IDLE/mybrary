package kr.mybrary.userservice.user.domain.dto.request;

import kr.mybrary.userservice.user.presentation.dto.request.UserInfoRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserInfoServiceRequest {

    List<String> userIds;

    public static UserInfoServiceRequest of(UserInfoRequest userInfoRequest) {
        return UserInfoServiceRequest.builder()
                .userIds(userInfoRequest.getUserIds())
                .build();
    }

}
