package kr.mybrary.userservice.authentication;

import jakarta.servlet.http.HttpServletRequest;
import kr.mybrary.userservice.global.dto.response.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping("/test")
    public ResponseEntity<SuccessResponse> signUp(HttpServletRequest request) {
        String userId = request.getAttribute("USER-ID").toString();
        return ResponseEntity.status(200).body(
                SuccessResponse.of(HttpStatus.CREATED.toString(), "인가된 사용자입니다. 로그인된 아이디: " + userId, null)
        );
    }

}
