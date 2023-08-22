package kr.mybrary.userservice.user.presentation;

import jakarta.validation.Valid;
import kr.mybrary.userservice.global.dto.response.FeignClientResponse;
import kr.mybrary.userservice.global.dto.response.SuccessResponse;
import kr.mybrary.userservice.user.domain.UserService;
import kr.mybrary.userservice.user.domain.dto.request.*;
import kr.mybrary.userservice.user.domain.dto.response.*;
import kr.mybrary.userservice.user.presentation.dto.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<SuccessResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        SignUpServiceRequest serviceRequest = SignUpServiceRequest.of(signUpRequest);
        SignUpServiceResponse serviceResponse = userService.signUp(serviceRequest);

        return ResponseEntity.status(200).body(
                SuccessResponse.of(HttpStatus.CREATED.toString(), "회원 가입에 성공했습니다.", serviceResponse)
        );
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<SuccessResponse> getProfile(@PathVariable("userId") String userId) {
        ProfileServiceResponse serviceResponse = userService.getProfile(userId);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "사용자의 프로필 정보입니다.",
                        serviceResponse)
        );
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<SuccessResponse> updateProfile(@PathVariable("userId") String userId,
                                                         @RequestHeader("USER-ID") String loginId,
                                                         @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        ProfileUpdateServiceRequest serviceRequest = ProfileUpdateServiceRequest.of(
                profileUpdateRequest, userId, loginId);
        ProfileServiceResponse serviceResponse = userService.updateProfile(serviceRequest);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "로그인 된 사용자의 프로필 정보를 수정했습니다.",
                        serviceResponse)
        );
    }

    @GetMapping("/{userId}/profile/image")
    public ResponseEntity<SuccessResponse> getProfileImageUrl(@PathVariable("userId") String userId,
                                                              @RequestParam(value = "size", required = false, defaultValue = "original") String size) {
        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "사용자의 프로필 이미지 URL입니다.",
                        userService.getProfileImageUrl(ProfileImageUrlServiceRequest.of(userId, size))));
    }

    @PutMapping("/{userId}/profile/image")
    public ResponseEntity<SuccessResponse> updateProfileImage(@PathVariable("userId") String userId,
                                                              @RequestHeader("USER-ID") String loginId,
                                                              @RequestParam("profileImage") MultipartFile profileImage) {
        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "로그인 된 사용자의 프로필 이미지를 등록했습니다.",
                        userService.updateProfileImage(ProfileImageUpdateServiceRequest.of(profileImage, loginId, userId, LocalDateTime.now()))));
    }

    @DeleteMapping("/{userId}/profile/image")
    public ResponseEntity<SuccessResponse> deleteProfileImage(@PathVariable("userId") String userId,
                                                              @RequestHeader("USER-ID") String loginId) {
        ProfileImageUrlServiceResponse serviceResponse = userService.deleteProfileImage(
                ProfileImageUpdateServiceRequest.of(loginId, userId));

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "로그인 된 사용자의 프로필 이미지를 삭제했습니다.",
                        serviceResponse)
        );
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<SuccessResponse> getFollowers(@PathVariable("userId") String userId) {
        FollowerServiceResponse serviceResponse = userService.getFollowers(userId);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "사용자의 팔로워 목록을 조회했습니다.",
                        serviceResponse)
        );
    }

    @GetMapping("/{userId}/followings")
    public ResponseEntity<SuccessResponse> getFollowings(@PathVariable("userId") String userId) {
        FollowingServiceResponse serviceResponse = userService.getFollowings(userId);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "사용자의 팔로잉 목록을 조회했습니다.",
                        serviceResponse)
        );
    }

    @PostMapping("/follow")
    public ResponseEntity<SuccessResponse> follow(@RequestHeader("USER-ID") String loginId,
                                                  @RequestBody FollowRequest followRequest) {
        String targetId = followRequest.getTargetId();
        FollowServiceRequest serviceRequest = FollowServiceRequest.of(loginId, targetId);
        userService.follow(serviceRequest);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "사용자를 팔로우했습니다.", null)
        );
    }

    @DeleteMapping("/follow")
    public ResponseEntity<SuccessResponse> unfollow(@RequestHeader("USER-ID") String loginId,
                                                    @RequestBody FollowRequest followRequest) {
        String targetId = followRequest.getTargetId();
        FollowServiceRequest serviceRequest = FollowServiceRequest.of(loginId, targetId);
        userService.unfollow(serviceRequest);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "사용자를 언팔로우했습니다.", null)
        );
    }

    @DeleteMapping("/follower")
    public ResponseEntity<SuccessResponse> unfollowing(@RequestHeader("USER-ID") String loginId,
                                                       @RequestBody FollowerRequest followerRequest) {
        String sourceId = followerRequest.getSourceId();
        FollowerServiceRequest serviceRequest = FollowerServiceRequest.of(loginId, sourceId);
        userService.deleteFollower(serviceRequest);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "사용자를 팔로워 목록에서 삭제했습니다.", null)
        );
    }

    @GetMapping("/follow")
    public ResponseEntity<SuccessResponse> isFollowing(@RequestHeader("USER-ID") String loginId,
                                                       @RequestParam("targetId") String targetId) {
        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "사용자를 팔로우 중인지 확인했습니다.",
                        userService.getFollowStatus(FollowServiceRequest.of(loginId, targetId)))
        );
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse> searchByNickname(@RequestParam(value = "nickname") String nickname) {
        SearchServiceResponse serviceResponse = userService.searchByNickname(nickname);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "닉네임으로 사용자를 검색했습니다.",
                        serviceResponse)
        );
    }

    @DeleteMapping("/account")
    public ResponseEntity<SuccessResponse> withdrawal(@RequestHeader("USER-ID") String loginId) {
        userService.deleteAccount(loginId);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "회원 탈퇴에 성공했습니다.", null)
        );
    }

    @PostMapping("/info")
    public ResponseEntity<FeignClientResponse> getUserInfoCalledByFeignClient(@RequestBody UserInfoRequest userInfoRequest) {
        return ResponseEntity.ok().body(
                FeignClientResponse.of(userService.getUserInfo(UserInfoServiceRequest.of(userInfoRequest)))
        );
    }

}
