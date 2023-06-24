package kr.mybrary.bookservice.global.exception;

public class ApplicationException extends RuntimeException {

    private int status;
    private String errorCode;
    private String errorMessage;

    protected ApplicationException(int status, String errorCode, String errorMessage) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
