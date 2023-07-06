package kr.mybrary.userservice.user.domain.dto.request;

import kr.mybrary.userservice.user.presentation.dto.request.SignUpRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpServiceRequest {

    private String loginId;
    private String password;
    private String nickname;
    private String email;

    public static SignUpServiceRequest of(SignUpRequest request) {
        return SignUpServiceRequest.builder()
                .loginId(request.getLoginId())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .build();
    }

}
