package kr.mybrary.userservice.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUpdateRequest {

    @NotNull
    @NotBlank(message = "닉네임은 필수입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,20}$", message = "닉네임은 특수문자를 제외한 2~20자리여야 합니다.")
    private String nickname;

    @Length(max = 100, message = "소개는 100자 이내로 작성해주세요.")
    private String introduction;

}
