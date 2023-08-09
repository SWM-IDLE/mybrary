package kr.mybrary.bookservice.client.user.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoRequest {

    List<String> usersIds;

    public static UserInfoRequest of(List<String> usersIds) {
        return UserInfoRequest.builder()
            .usersIds(usersIds)
            .build();
    }
}
