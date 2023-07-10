package kr.mybrary.userservice.user.domain;

import kr.mybrary.userservice.user.domain.dto.request.FollowServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.FollowerServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.ProfileImageUpdateServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.ProfileUpdateServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.userservice.user.domain.dto.response.FollowerServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.FollowingServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.ProfileImageUrlServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.ProfileServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.SignUpServiceResponse;

public interface UserService {

    SignUpServiceResponse signUp(SignUpServiceRequest signUpRequest);

    ProfileServiceResponse getProfile(String loginId);

    ProfileServiceResponse updateProfile(ProfileUpdateServiceRequest serviceRequest);

    ProfileImageUrlServiceResponse getProfileImageUrl(String loginId);

    ProfileImageUrlServiceResponse updateProfileImage(ProfileImageUpdateServiceRequest serviceRequest);

    ProfileImageUrlServiceResponse deleteProfileImage(String loginId);

    FollowerServiceResponse getFollowers(String loginId);

    FollowingServiceResponse getFollowings(String loginId);

    void follow(FollowServiceRequest serviceRequest);

    void unfollow(FollowServiceRequest serviceRequest);

    void deleteFollower(FollowerServiceRequest serviceRequest);
}
