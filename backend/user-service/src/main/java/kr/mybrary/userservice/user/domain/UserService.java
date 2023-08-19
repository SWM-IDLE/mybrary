package kr.mybrary.userservice.user.domain;

import kr.mybrary.userservice.user.domain.dto.request.*;
import kr.mybrary.userservice.user.domain.dto.response.*;

public interface UserService {

    SignUpServiceResponse signUp(SignUpServiceRequest signUpRequest);

    UserResponse getUserResponse(String loginId);

    ProfileServiceResponse getProfile(String loginId);

    ProfileServiceResponse updateProfile(ProfileUpdateServiceRequest serviceRequest);

    ProfileImageUrlServiceResponse getProfileImageUrl(ProfileImageUrlServiceRequest serviceRequest);

    ProfileImageUrlServiceResponse updateProfileImage(ProfileImageUpdateServiceRequest serviceRequest);

    ProfileImageUrlServiceResponse deleteProfileImage(ProfileImageUpdateServiceRequest serviceRequest);

    FollowerServiceResponse getFollowers(String loginId);

    FollowingServiceResponse getFollowings(String loginId);

    void follow(FollowServiceRequest serviceRequest);

    void unfollow(FollowServiceRequest serviceRequest);

    void deleteFollower(FollowerServiceRequest serviceRequest);

    FollowStatusServiceResponse getFollowStatus(FollowServiceRequest serviceRequest);

    SearchServiceResponse searchByNickname(String nickname);

    void deleteAccount(String loginId);

    UserInfoServiceResponse getUserInfo(UserInfoServiceRequest serviceRequest);

}
