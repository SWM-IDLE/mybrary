package kr.mybrary.userservice.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private String errorCode;
    private String errorMessage;

    public CustomException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
