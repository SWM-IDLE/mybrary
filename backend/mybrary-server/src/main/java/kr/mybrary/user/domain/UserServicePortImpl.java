package kr.mybrary.user.domain;

import kr.mybrary.user.domain.dto.request.UserInfoServiceRequest;
import kr.mybrary.user.domain.dto.response.UserInfoServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * UserServicePort 구현체.
 * book-service에서 Feign으로 호출했던 /api/v1/users/info 엔드포인트를
 * UserService를 직접 호출하는 방식으로 대체한다.
 */
@Component
@RequiredArgsConstructor
public class UserServicePortImpl implements UserServicePort {

    private final UserService userService;

    @Override
    public UserInfoServiceResponse getUsersInfo(UserInfoServiceRequest request) {
        return userService.getUserInfo(request);
    }

}
