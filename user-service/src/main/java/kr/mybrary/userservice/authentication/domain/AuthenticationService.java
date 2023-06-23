package kr.mybrary.userservice.authentication.domain;

import kr.mybrary.userservice.authentication.domain.dto.SignUpResponse;
import kr.mybrary.userservice.authentication.presentation.dto.request.SignUpRequest;

public interface AuthenticationService {

    public SignUpResponse signUp(SignUpRequest signUpRequest);


}
