package kr.mybrary.user.domain.dto.response;

import kr.mybrary.user.persistence.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    User user;

}
