package kr.mybrary.userservice.user.presentation.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileUpdateRequestValidationTest {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("닉네임은 필수이다.")
    @Test
    void profileUpdateValidationTest() {
        // given
        ProfileUpdateRequest profileUpdateRequest = ProfileUpdateRequest.builder()
                .nickname("")
                .build();

        // when
        Set<ConstraintViolation<ProfileUpdateRequest>> constraintViolations = validator.validate(profileUpdateRequest);

        // then
        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("닉네임은 필수입니다.");
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
                .introduction(generateRandomString(101))
                .build();

        // when
        Set<ConstraintViolation<ProfileUpdateRequest>> constraintViolations = validator.validate(profileUpdateRequest);

        // then
        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("소개는 100자 이내로 작성해주세요.");
    }

    private String generateRandomString(int targetLength) {
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetLength);
        for (int i = 0; i < targetLength; i++) {
            buffer.append((char) ('a' + random.nextInt('z' - 'a' + 1)));
        }
        return buffer.toString();
    }

}
