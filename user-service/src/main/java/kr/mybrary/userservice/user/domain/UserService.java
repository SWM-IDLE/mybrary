package kr.mybrary.userservice.user.domain;

import kr.mybrary.userservice.user.domain.dto.response.ProfileServiceResponse;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.presentation.dto.request.SignUpRequest;
import kr.mybrary.userservice.user.presentation.dto.response.SignUpResponse;

public interface UserService {

    public SignUpResponse signUp(SignUpRequest signUpRequest);

    public User grantUserRole(String loginId);

    public ProfileServiceResponse getProfile(String loginId);


}
