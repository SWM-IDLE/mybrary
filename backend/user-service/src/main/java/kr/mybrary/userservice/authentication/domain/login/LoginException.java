package kr.mybrary.userservice.authentication.domain.login;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginException {

    LOGIN_ID_NOT_FOUND("U-05", "존재하지 않는 아이디입니다: %s"),
    PASSWORD_NOT_MATCH("U-06", "비밀번호가 일치하지 않습니다."),
    CONTENT_TYPE_NOT_JSON("U-07", "지원되지 않는 Content-Type 입니다: %s. JSON 형식으로 요청해주세요.");

    private final String errorCode;
    private final String errorMessage;

}
