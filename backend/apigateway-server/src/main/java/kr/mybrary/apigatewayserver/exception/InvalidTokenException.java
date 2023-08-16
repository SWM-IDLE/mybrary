package kr.mybrary.apigatewayserver.exception;

public class InvalidTokenException extends ApplicationException {

    private final static int STATUS = 400;
    private final static String ERROR_CODE = "T-03";
    private final static String ERROR_MESSAGE = "token is invalid";

    public InvalidTokenException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
