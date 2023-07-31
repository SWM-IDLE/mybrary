package kr.mybrary.userservice.user.domain.dto.response;

import kr.mybrary.userservice.user.persistence.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    User user;

}
