package kr.mybrary.userservice.authentication.presentation;

import jakarta.validation.Valid;
import kr.mybrary.userservice.authentication.domain.AuthenticationService;
import kr.mybrary.userservice.authentication.presentation.dto.request.SignUpRequest;
import kr.mybrary.userservice.authentication.presentation.dto.response.SignUpResponse;
import kr.mybrary.userservice.global.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public ResponseEntity<SuccessResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        SignUpResponse response = authenticationService.signUp(signUpRequest);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.CREATED.toString(), "회원 가입에 성공했습니다.", response)
        );
    }

}
