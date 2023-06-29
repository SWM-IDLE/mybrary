package kr.mybrary.userservice.authentication.domain;

import kr.mybrary.userservice.authentication.presentation.dto.response.SignUpResponse;
import kr.mybrary.userservice.authentication.presentation.dto.request.SignUpRequest;
import kr.mybrary.userservice.user.persistence.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticationService extends UserDetailsService {

    public SignUpResponse signUp(SignUpRequest signUpRequest);

    public User authorizeUser(String loginId);

}
