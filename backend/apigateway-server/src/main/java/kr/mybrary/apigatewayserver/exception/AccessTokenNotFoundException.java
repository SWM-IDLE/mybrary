package kr.mybrary.apigatewayserver.exception;

public class AccessTokenNotFoundException extends ApplicationException {

    private final static int STATUS = 401;
    private final static String ERROR_CODE = "T-01";
    private final static String ERROR_MESSAGE = "액세스 토큰이 존재하지 않습니다. 로그인이 필요합니다.";

    public AccessTokenNotFoundException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
