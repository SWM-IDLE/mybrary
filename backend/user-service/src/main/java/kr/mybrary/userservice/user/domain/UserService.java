package kr.mybrary.userservice.user.domain;

import kr.mybrary.userservice.user.domain.dto.request.FollowServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.FollowerServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.ProfileImageUpdateServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.ProfileUpdateServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.userservice.user.domain.dto.response.*;

public interface UserService {

    SignUpServiceResponse signUp(SignUpServiceRequest signUpRequest);

    UserResponse getUserResponse(String loginId);

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

    SearchServiceResponse searchByNickname(String nickname);

}
