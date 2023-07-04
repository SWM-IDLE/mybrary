package kr.mybrary.userservice.user.domain;

import kr.mybrary.userservice.user.domain.dto.ProfileResponse;

public interface UserService {

    public ProfileResponse getProfile(String loginId);


}
