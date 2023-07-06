package kr.mybrary.userservice.user.domain;

import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.userservice.user.domain.dto.response.ProfileServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.SignUpServiceResponse;
import kr.mybrary.userservice.user.persistence.User;

public interface UserService {

    SignUpServiceResponse signUp(SignUpServiceRequest signUpRequest);

    User grantUserRole(String loginId);

    ProfileServiceResponse getProfile(String loginId);


}
