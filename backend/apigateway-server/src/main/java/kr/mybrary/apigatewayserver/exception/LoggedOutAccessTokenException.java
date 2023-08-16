package kr.mybrary.apigatewayserver.exception;

public class LoggedOutAccessTokenException extends ApplicationException{

    private final static int STATUS = 400;
    private final static String ERROR_CODE = "T-02";
    private final static String ERROR_MESSAGE = "access token is logged out";

    public LoggedOutAccessTokenException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
