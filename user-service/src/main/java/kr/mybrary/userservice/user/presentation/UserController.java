package kr.mybrary.userservice.user.presentation;

import jakarta.validation.Valid;
import kr.mybrary.userservice.global.dto.response.SuccessResponse;
import kr.mybrary.userservice.user.domain.UserService;
import kr.mybrary.userservice.user.domain.dto.request.ProfileImageServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.ProfileUpdateServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.userservice.user.domain.dto.response.ProfileImageServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.ProfileServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.SignUpServiceResponse;
import kr.mybrary.userservice.user.presentation.dto.request.ProfileImageUpdateRequest;
import kr.mybrary.userservice.user.presentation.dto.request.ProfileUpdateRequest;
import kr.mybrary.userservice.user.presentation.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /*
    - Profile API 문서 작성
        - 프로필 조회: get users/profile
        - 프로필 수정: put users/profile
        - 프로필 이미지 조회: get users/profile/image
        - 프로필 이미지 등록: put users/profile/image
        - 프로필 이미지 삭제: delete users/profile/image
     */

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
            @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        ProfileUpdateServiceRequest serviceRequest = ProfileUpdateServiceRequest.of(profileUpdateRequest, loginId);
        ProfileServiceResponse serviceResponse = userService.updateProfile(serviceRequest);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "로그인 된 사용자의 프로필 정보를 수정했습니다.",
                        serviceResponse)
        );
    }

    @GetMapping("/profile/image")
    public ResponseEntity<SuccessResponse> getProfileImage(
            @RequestHeader("USER-ID") String loginId) {
        ProfileImageServiceResponse serviceResponse = userService.getProfileImage(loginId);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "로그인 된 사용자의 프로필 이미지 URL입니다.",
                        serviceResponse)
        );
    }

    @PostMapping("/profile/image")
    public ResponseEntity<SuccessResponse> updateProfileImage(
            @RequestHeader("USER-ID") String loginId,
            @ModelAttribute ProfileImageUpdateRequest profileImageUpdateRequest) {
        ProfileImageServiceRequest serviceRequest = ProfileImageServiceRequest.of(
                profileImageUpdateRequest, loginId);
        ProfileImageServiceResponse serviceResponse = userService.updateProfileImage(
                serviceRequest);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "로그인 된 사용자의 프로필 이미지를 등록했습니다.",
                        serviceResponse)
        );
    }

    @DeleteMapping("/profile/image")
    public ResponseEntity<SuccessResponse> deleteProfileImage(
            @RequestHeader("USER-ID") String loginId) {
        ProfileImageServiceResponse serviceResponse = userService.deleteProfileImage(loginId);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "로그인 된 사용자의 프로필 이미지를 삭제했습니다.", serviceResponse)
        );
    }

    /*
    - Follow API 문서 작성
        - 팔로워 목록 조회: get users/followers
        - 팔로잉 목록 조회: get users/followings
        - 팔로우: post users/follow
        - 언팔로우: delete users/follow
     */

}
