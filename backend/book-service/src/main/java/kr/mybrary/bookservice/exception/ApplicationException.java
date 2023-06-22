package kr.mybrary.bookservice.exception;

public class ApplicationException extends RuntimeException {

    private String errorCode;
    private String errorMessage;

    public ApplicationException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
