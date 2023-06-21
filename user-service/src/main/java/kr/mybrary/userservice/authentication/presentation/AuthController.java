package kr.mybrary.userservice.authentication.presentation;

import jakarta.validation.Valid;
import kr.mybrary.userservice.authentication.application.AuthenticationService;
import kr.mybrary.userservice.authentication.presentation.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@Valid @RequestBody SignUpRequest signUpRequest) {

        authenticationService.signUp(signUpRequest);
        return new ResponseEntity(HttpStatus.OK);

    }
}
