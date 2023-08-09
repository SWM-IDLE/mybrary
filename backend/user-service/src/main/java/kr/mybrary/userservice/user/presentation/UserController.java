package kr.mybrary.userservice.user.presentation;

import jakarta.validation.Valid;
import kr.mybrary.userservice.global.dto.response.SuccessResponse;
import kr.mybrary.userservice.user.domain.UserService;
import kr.mybrary.userservice.user.domain.dto.request.*;
import kr.mybrary.userservice.user.domain.dto.response.*;
import kr.mybrary.userservice.user.presentation.dto.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

     @GetMapping("/profile")
    public ResponseEntity<SuccessResponse> getProfile(@RequestHeader("USER-ID") String loginId) {
        ProfileServiceResponse serviceResponse = userService.getProfile(loginId);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "로그인 된 사용자의 프로필 정보입니다.",
                        serviceResponse)
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<SuccessResponse> updateProfile(@RequestHeader("USER-ID") String loginId,
            @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        ProfileUpdateServiceRequest serviceRequest = ProfileUpdateServiceRequest.of(
                profileUpdateRequest, loginId);
        ProfileServiceResponse serviceResponse = userService.updateProfile(serviceRequest);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "로그인 된 사용자의 프로필 정보를 수정했습니다.",
                        serviceResponse)
        );
    }

    @GetMapping("/profile/image")
    public ResponseEntity<SuccessResponse> getProfileImageUrl(
            @RequestHeader("USER-ID") String loginId) {
        ProfileImageUrlServiceResponse serviceResponse = userService.getProfileImageUrl(loginId);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "로그인 된 사용자의 프로필 이미지 URL입니다.",
                        serviceResponse)
        );
    }

    @PutMapping("/profile/image")
    public ResponseEntity<SuccessResponse> updateProfileImage(
            @RequestHeader("USER-ID") String loginId, @RequestParam("profileImage") MultipartFile profileImage) {
        ProfileImageUpdateServiceRequest serviceRequest = ProfileImageUpdateServiceRequest.of(
                profileImage, loginId);
        ProfileImageUrlServiceResponse serviceResponse = userService.updateProfileImage(
                serviceRequest);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "로그인 된 사용자의 프로필 이미지를 등록했습니다.",
                        serviceResponse)
        );
    }

    @DeleteMapping("/profile/image")
    public ResponseEntity<SuccessResponse> deleteProfileImage(
            @RequestHeader("USER-ID") String loginId) {
        ProfileImageUrlServiceResponse serviceResponse = userService.deleteProfileImage(loginId);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "로그인 된 사용자의 프로필 이미지를 삭제했습니다.",
                        serviceResponse)
        );
    }

    @GetMapping("/followers")
    public ResponseEntity<SuccessResponse> getFollowers(@RequestHeader("USER-ID") String loginId) {
        FollowerServiceResponse serviceResponse = userService.getFollowers(loginId);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "로그인 된 사용자의 팔로워 목록을 조회했습니다.",
                        serviceResponse)
        );
    }

    @GetMapping("/followings")
    public ResponseEntity<SuccessResponse> getFollowings(@RequestHeader("USER-ID") String loginId) {
        FollowingServiceResponse serviceResponse = userService.getFollowings(loginId);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "로그인 된 사용자의 팔로잉 목록을 조회했습니다.",
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

    @GetMapping("/info")
    public ResponseEntity<SuccessResponse> getUserInfo(@RequestBody UserInfoRequest userInfoRequest) {
        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "사용자 정보를 모두 조회했습니다.",
                        userService.getUserInfo(UserInfoServiceRequest.of(userInfoRequest)))
        );
    }

}
