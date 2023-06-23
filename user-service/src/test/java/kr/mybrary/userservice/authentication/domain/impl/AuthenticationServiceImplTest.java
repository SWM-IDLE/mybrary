package kr.mybrary.userservice.authentication.domain.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import kr.mybrary.userservice.authentication.domain.dto.SignUpResponse;
import kr.mybrary.userservice.authentication.domain.dto.UserMapper;
import kr.mybrary.userservice.authentication.domain.exception.DuplicateUserInfoException;
import kr.mybrary.userservice.authentication.presentation.dto.request.SignUpRequest;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AuthenticationServiceImpl authenticationService;

    @Test
    @DisplayName("회원가입 요청이 들어오면 암호화된 비밀번호와 함께 회원 권한으로 사용자 정보를 저장한다")
    void signUp() {
        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId("loginId")
                .password("password123!")
                .nickname("nickname")
                .email("email@mail.com")
                .build();

        given(userRepository.findByLoginId(signUpRequest.getLoginId())).willReturn(Optional.empty());
        given(userRepository.findByNickname(signUpRequest.getNickname())).willReturn(Optional.empty());
        given(userRepository.findByEmail(signUpRequest.getEmail())).willReturn(Optional.empty());
        given(passwordEncoder.encode(signUpRequest.getPassword())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(
                User.builder()
                        .loginId(signUpRequest.getLoginId())
                        .password("encodedPassword")
                        .nickname(signUpRequest.getNickname())
                        .email(signUpRequest.getEmail())
                        .role(Role.USER)
                        .build()
        );

        // When
        SignUpResponse savedUser = authenticationService.signUp(signUpRequest);

        // Then
        Assertions.assertAll(
                () -> assertThat(savedUser.getLoginId()).isEqualTo(signUpRequest.getLoginId()),
                () -> assertThat(savedUser.getNickname()).isEqualTo(signUpRequest.getNickname()),
                () -> assertThat(savedUser.getEmail()).isEqualTo(signUpRequest.getEmail()),
                () -> assertThat(savedUser.getPassword()).isEqualTo("encodedPassword"),
                () -> assertThat(savedUser.getRole()).isEqualTo(Role.USER)
        );

        verify(userRepository).findByLoginId(signUpRequest.getLoginId());
        verify(userRepository).findByEmail(signUpRequest.getEmail());
        verify(userRepository).findByNickname(signUpRequest.getNickname());
        verify(userRepository).save(any(User.class));

    }

    @Test
    @DisplayName("아이디가 중복되면 예외를 던진다")
    void DuplicateLoginId() {
        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId("loginId")
                .password("password123!")
                .nickname("nickname")
                .email("email@mail.com")
                .build();

        User user = User.builder()
                .loginId("loginId")
                .build();

        given(userRepository.findByLoginId(signUpRequest.getLoginId())).willReturn(
                Optional.of(user));

        // When
        assertThatThrownBy(() -> authenticationService.signUp(signUpRequest))
                .isInstanceOf(DuplicateUserInfoException.class)
                .hasFieldOrPropertyWithValue("errorCode", "U-01")
                .hasFieldOrPropertyWithValue("errorMessage", "이미 존재하는 회원입니다.");

        // Then
        verify(userRepository).findByLoginId(signUpRequest.getLoginId());
    }

    @Test
    @DisplayName("닉네임이 중복되면 예외를 던진다")
    void DuplicateNickname() {
        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId("loginId")
                .password("password123!")
                .nickname("nickname")
                .email("email@mail.com")
                .build();

        User user = User.builder()
                .nickname("nickname")
                .build();

        given(userRepository.findByNickname(signUpRequest.getNickname())).willReturn(
                Optional.of(user));

        // When
        assertThatThrownBy(() -> authenticationService.signUp(signUpRequest))
                .isInstanceOf(DuplicateUserInfoException.class)
                .hasFieldOrPropertyWithValue("errorCode", "U-01")
                .hasFieldOrPropertyWithValue("errorMessage", "이미 존재하는 회원입니다.");

        // Then
        verify(userRepository).findByNickname(signUpRequest.getNickname());
    }

    @Test
    @DisplayName("이메일이 중복되면 예외를 던진다")
    void DuplicateEmail() {
        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId("loginId")
                .password("password123!")
                .nickname("nickname")
                .email("email@mail.com")
                .build();

        User user = User.builder()
                .email("email@mail.com")
                .build();

        given(userRepository.findByEmail(signUpRequest.getEmail())).willReturn(
                Optional.of(user));

        // When
        assertThatThrownBy(() -> authenticationService.signUp(signUpRequest))
                .isInstanceOf(DuplicateUserInfoException.class)
                .hasFieldOrPropertyWithValue("errorCode", "U-01")
                .hasFieldOrPropertyWithValue("errorMessage", "이미 존재하는 회원입니다.");

        // Then
        verify(userRepository).findByEmail(signUpRequest.getEmail());
    }

}