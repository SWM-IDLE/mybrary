package kr.mybrary.userservice.user.presentation;

import kr.mybrary.userservice.global.dto.response.SuccessResponse;
import kr.mybrary.userservice.user.domain.UserService;
import kr.mybrary.userservice.user.domain.dto.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<SuccessResponse> getProfile(@RequestHeader("USER-ID") String loginId) {
        ProfileResponse response = userService.getProfile(loginId);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "로그인 된 사용자의 프로필 정보입니다.", response)
        );
    }

}
