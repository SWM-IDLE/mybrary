package kr.mybrary.userservice.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.userservice.user.domain.dto.response.SignUpServiceResponse;
import kr.mybrary.userservice.user.domain.exception.DuplicateEmailException;
import kr.mybrary.userservice.user.domain.exception.DuplicateLoginIdException;
import kr.mybrary.userservice.user.domain.exception.DuplicateNicknameException;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("회원가입 요청이 들어오면 암호화된 비밀번호와 함께 회원 권한으로 사용자 정보를 저장한다")
    void signUp() {
        // Given
        SignUpServiceRequest serviceRequest = SignUpServiceRequest.builder()
                .loginId("loginId")
                .password("password123!")
                .nickname("nickname")
                .email("email@mail.com")
                .build();

        given(userRepository.findByLoginId(serviceRequest.getLoginId())).willReturn(Optional.empty());
        given(userRepository.findByNickname(serviceRequest.getNickname())).willReturn(Optional.empty());
        given(userRepository.findByEmail(serviceRequest.getEmail())).willReturn(Optional.empty());
        given(passwordEncoder.encode(serviceRequest.getPassword())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(
                User.builder()
                        .loginId(serviceRequest.getLoginId())
                        .password("encodedPassword")
                        .nickname(serviceRequest.getNickname())
                        .email(serviceRequest.getEmail())
                        .role(Role.USER)
                        .build()
        );

        // When
        SignUpServiceResponse savedUser = userService.signUp(serviceRequest);

        // Then
        Assertions.assertAll(
                () -> assertThat(savedUser.getLoginId()).isEqualTo(serviceRequest.getLoginId()),
                () -> assertThat(savedUser.getNickname()).isEqualTo(serviceRequest.getNickname()),
                () -> assertThat(savedUser.getEmail()).isEqualTo(serviceRequest.getEmail()),
                () -> assertThat(savedUser.getRole()).isEqualTo(Role.USER)
        );

        verify(userRepository).findByLoginId(serviceRequest.getLoginId());
        verify(userRepository).findByEmail(serviceRequest.getEmail());
        verify(userRepository).findByNickname(serviceRequest.getNickname());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("아이디가 중복되면 예외를 던진다")
    void DuplicateLoginId() {
        // Given
        SignUpServiceRequest serviceRequest = SignUpServiceRequest.builder()
                .loginId("loginId")
                .password("password123!")
                .nickname("nickname")
                .email("email@mail.com")
                .build();

        User user = User.builder()
                .loginId("loginId")
                .build();

        given(userRepository.findByLoginId(serviceRequest.getLoginId())).willReturn(
                Optional.of(user));

        // When
        assertThatThrownBy(() -> userService.signUp(serviceRequest))
                .isInstanceOf(DuplicateLoginIdException.class)
                .hasFieldOrPropertyWithValue("errorCode", "U-02")
                .hasFieldOrPropertyWithValue("errorMessage", "이미 존재하는 로그인 아이디입니다.");

        // Then
        verify(userRepository).findByLoginId(serviceRequest.getLoginId());
    }

    @Test
    @DisplayName("닉네임이 중복되면 예외를 던진다")
    void DuplicateNickname() {
        // Given
        SignUpServiceRequest serviceRequest = SignUpServiceRequest.builder()
                .loginId("loginId")
                .password("password123!")
                .nickname("nickname")
                .email("email@mail.com")
                .build();

        User user = User.builder()
                .nickname("nickname")
                .build();

        given(userRepository.findByNickname(serviceRequest.getNickname())).willReturn(
                Optional.of(user));

        // When
        assertThatThrownBy(() -> userService.signUp(serviceRequest))
                .isInstanceOf(DuplicateNicknameException.class)
                .hasFieldOrPropertyWithValue("errorCode", "U-03")
                .hasFieldOrPropertyWithValue("errorMessage", "이미 존재하는 닉네임입니다.");

        // Then
        verify(userRepository).findByNickname(serviceRequest.getNickname());
    }

    @Test
    @DisplayName("이메일이 중복되면 예외를 던진다")
    void DuplicateEmail() {
        // Given
        SignUpServiceRequest serviceRequest = SignUpServiceRequest.builder()
                .loginId("loginId")
                .password("password123!")
                .nickname("nickname")
                .email("email@mail.com")
                .build();

        User user = User.builder()
                .email("email@mail.com")
                .build();

        given(userRepository.findByEmail(serviceRequest.getEmail())).willReturn(
                Optional.of(user));

        // When
        assertThatThrownBy(() -> userService.signUp(serviceRequest))
                .isInstanceOf(DuplicateEmailException.class)
                .hasFieldOrPropertyWithValue("errorCode", "U-04")
                .hasFieldOrPropertyWithValue("errorMessage", "이미 존재하는 이메일입니다.");

        // Then
        verify(userRepository).findByEmail(serviceRequest.getEmail());
    }

}