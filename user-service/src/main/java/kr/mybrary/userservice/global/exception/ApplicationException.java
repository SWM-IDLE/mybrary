package kr.mybrary.userservice.global.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private String errorCode;
    private String errorMessage;

    public ApplicationException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
