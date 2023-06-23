package kr.mybrary.userservice.authentication.domain;

import kr.mybrary.userservice.authentication.presentation.dto.response.SignUpResponse;
import kr.mybrary.userservice.authentication.presentation.dto.request.SignUpRequest;

public interface AuthenticationService {

    public SignUpResponse signUp(SignUpRequest signUpRequest);


}
