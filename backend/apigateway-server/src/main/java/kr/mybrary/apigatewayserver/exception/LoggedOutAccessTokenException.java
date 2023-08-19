package kr.mybrary.apigatewayserver.exception;

public class LoggedOutAccessTokenException extends ApplicationException{

    private final static int STATUS = 401;
    private final static String ERROR_CODE = "T-02";
    private final static String ERROR_MESSAGE = "로그아웃된 액세스 토큰입니다. 로그인이 필요합니다.";

    public LoggedOutAccessTokenException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
