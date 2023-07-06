package kr.mybrary.userservice.user.domain;

import kr.mybrary.userservice.user.domain.dto.request.ProfileImageServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.ProfileUpdateServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.userservice.user.domain.dto.response.ProfileImageServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.ProfileServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.SignUpServiceResponse;

public interface UserService {

    SignUpServiceResponse signUp(SignUpServiceRequest signUpRequest);

    ProfileServiceResponse getProfile(String loginId);

    ProfileServiceResponse updateProfile(ProfileUpdateServiceRequest serviceRequest);

    ProfileImageServiceResponse getProfileImage(String loginId);

    ProfileImageServiceResponse updateProfileImage(ProfileImageServiceRequest serviceRequest);

    ProfileImageServiceResponse deleteProfileImage(String loginId);
}
