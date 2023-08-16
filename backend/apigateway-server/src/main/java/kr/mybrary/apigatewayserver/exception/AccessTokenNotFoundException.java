package kr.mybrary.apigatewayserver.exception;

public class AccessTokenNotFoundException extends ApplicationException {

    private final static int STATUS = 400;
    private final static String ERROR_CODE = "T-01";
    private final static String ERROR_MESSAGE = "access token required";

    public AccessTokenNotFoundException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
