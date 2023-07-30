package kr.mybrary.userservice.global.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private int status;
    private String errorCode;
    private String errorMessage;

    public ApplicationException(int status, String errorCode, String errorMessage) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
