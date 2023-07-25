package kr.mybrary.userservice.global.exception;

import kr.mybrary.userservice.global.dto.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> applicationException(ApplicationException e) {
        return ResponseEntity.status(e.getStatus())
                .body(ErrorResponse.of(e.getErrorCode(), e.getErrorMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeException(RuntimeException e) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of("RT-01", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of("VLD-01", e.getBindingResult().getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.joining(", "))));
    }
}
