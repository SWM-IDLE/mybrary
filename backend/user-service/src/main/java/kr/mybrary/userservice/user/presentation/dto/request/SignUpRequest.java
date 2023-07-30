package kr.mybrary.userservice.user.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {

    @NotNull
    @NotBlank(message = "로그인 아이디는 필수입니다.")
    @Pattern(regexp = "^[A-Za-z0-9_-]{6,}$", message = "로그인 아이디는 6자 이상의 영문, 숫자 구성이어야 합니다. (하이픈과 언더바는 허용)")
    private String loginId;

    @NotNull
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}$", message = "비밀번호는 8~16자 영문, 숫자, 특수문자 구성이어야 합니다.")
    private String password;

    @NotNull
    @NotBlank(message = "닉네임은 필수입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{2,20}$", message = "닉네임은 특수문자를 제외한 2~20자리여야 합니다.")
    private String nickname;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

}
