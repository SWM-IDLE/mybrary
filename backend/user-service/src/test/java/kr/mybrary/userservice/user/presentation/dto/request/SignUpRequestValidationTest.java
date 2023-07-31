package kr.mybrary.userservice.user.presentation.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SignUpRequestValidationTest {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private static final String VALID_LOGIN_ID = "loginId";
    private static final String VALID_PASSWORD = "password123!";
    private static final String VALID_NICKNAME = "nickname";
    private static final String VALID_EMAIL = "email@mail.com";

    @DisplayName("로그인 아이디는 필수이다.")
    @Test
    void signUpValidationTest() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId("")
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD)
                .nickname(VALID_NICKNAME)
                .build();

        // when
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(signUpRequest);

        // then
        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("로그인 아이디는 필수입니다.");
    }

    @DisplayName("로그인 아이디는 6자 이상의 영문, 숫자 구성이어야 한다 (하이픈과 언더바는 허용)")
    @Test
    void signUpValidationTest2() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId("로그인아이디##")
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD)
                .nickname(VALID_NICKNAME)
                .build();

        // when
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(signUpRequest);

        // then
        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("로그인 아이디는 6자 이상의 영문, 숫자 구성이어야 합니다. (하이픈과 언더바는 허용)");
    }

    @DisplayName("비밀번호는 필수이다.")
    @Test
    void signUpValidationTest3() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId(VALID_LOGIN_ID)
                .email(VALID_EMAIL)
                .password("")
                .nickname(VALID_NICKNAME)
                .build();

        // when
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(signUpRequest);

        // then
        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("비밀번호는 필수입니다.");
    }

    @DisplayName("비밀번호는 8~16자 영문, 숫자, 특수문자 구성이어야 한다.")
    @Test
    void signUpValidationTest4() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId(VALID_LOGIN_ID)
                .email(VALID_EMAIL)
                .password("password 123!")
                .nickname(VALID_NICKNAME)
                .build();

        // when
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(signUpRequest);

        // then
        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("비밀번호는 8~16자 영문, 숫자, 특수문자 구성이어야 합니다.");
    }

    @DisplayName("닉네임은 필수이다.")
    @Test
    void signUpValidationTest5() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId(VALID_LOGIN_ID)
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD)
                .nickname("")
                .build();

        // when
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(signUpRequest);

        // then
        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("닉네임은 필수입니다.");
    }

    @DisplayName("닉네임은 특수문자를 제외한 2~20자리여야 한다.")
    @Test
    void signUpValidationTest6() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId(VALID_LOGIN_ID)
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD)
                .nickname("~")
                .build();

        // when
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(signUpRequest);

        // then
        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("닉네임은 특수문자를 제외한 2~20자리여야 합니다.");
    }

    @DisplayName("이메일은 이메일 형식에 맞아야 한다.")
    @Test
    void signUpValidationTest7() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId(VALID_LOGIN_ID)
                .email("invalid-email")
                .password(VALID_PASSWORD)
                .nickname(VALID_NICKNAME)
                .build();

        // when
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(signUpRequest);

        // then
        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("이메일 형식이 올바르지 않습니다.");
    }
}
