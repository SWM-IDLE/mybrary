package kr.mybrary.userservice.user.presentation.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileUpdateRequestValidationTest {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("닉네임은 필수이다.")
    @Test
    void profileUpdateValidationTest() {
        // given
        ProfileUpdateRequest profileUpdateRequest = ProfileUpdateRequest.builder()
                .nickname(null)
                .build();

        // when
        Set<ConstraintViolation<ProfileUpdateRequest>> constraintViolations = validator.validate(profileUpdateRequest);

        // then
        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("닉네임은 필수입니다.", "널이어서는 안됩니다");
    }

    @DisplayName("닉네임은 특수문자를 제외한 2~20자리여야 한다.")
    @Test
    void profileUpdateValidationTest2() {
        // given
        ProfileUpdateRequest profileUpdateRequest = ProfileUpdateRequest.builder()
                .nickname("닉네임##")
                .build();

        // when
        Set<ConstraintViolation<ProfileUpdateRequest>> constraintViolations = validator.validate(profileUpdateRequest);

        // then
        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("닉네임은 특수문자를 제외한 2~20자리여야 합니다.");
    }

    @DisplayName("소개는 100자 이내로 작성해야 한다.")
    @Test
    void profileUpdateValidationTest3() {
        // given
        ProfileUpdateRequest profileUpdateRequest = ProfileUpdateRequest.builder()
                .nickname("nickname")
                .introduction("############################################################" +
                        "############################################################")
                .build();

        // when
        Set<ConstraintViolation<ProfileUpdateRequest>> constraintViolations = validator.validate(profileUpdateRequest);

        // then
        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("소개는 100자 이내로 작성해주세요.");
    }

}