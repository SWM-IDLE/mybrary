package kr.mybrary.apigatewayserver.exception;

public class InvalidTokenException extends ApplicationException {

    private final static int STATUS = 401;
    private final static String ERROR_CODE = "T-03";
    private final static String ERROR_MESSAGE = "유효하지 않은 토큰입니다. 기간이 만료되었거나, 변조된 토큰입니다.";

    public InvalidTokenException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
