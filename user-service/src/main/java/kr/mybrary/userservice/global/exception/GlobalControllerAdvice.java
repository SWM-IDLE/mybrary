package kr.mybrary.userservice.global.exception;

import javax.security.sasl.AuthenticationException;
import kr.mybrary.userservice.global.dto.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> applicationException(ApplicationException e) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(e.getErrorCode(), e.getErrorMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeException(RuntimeException e) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of("RT-01", e.getMessage()));
    }
}
