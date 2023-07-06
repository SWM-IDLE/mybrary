package kr.mybrary.userservice.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import kr.mybrary.userservice.user.domain.exception.DuplicateEmailException;
import kr.mybrary.userservice.user.domain.exception.DuplicateLoginIdException;
import kr.mybrary.userservice.user.domain.exception.DuplicateNicknameException;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import kr.mybrary.userservice.user.presentation.dto.request.SignUpRequest;
import kr.mybrary.userservice.user.presentation.dto.response.SignUpResponse;
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
        SignUpResponse savedUser = userService.signUp(signUpRequest);

        // Then
        Assertions.assertAll(
                () -> assertThat(savedUser.getLoginId()).isEqualTo(signUpRequest.getLoginId()),
                () -> assertThat(savedUser.getNickname()).isEqualTo(signUpRequest.getNickname()),
                () -> assertThat(savedUser.getEmail()).isEqualTo(signUpRequest.getEmail()),
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
        assertThatThrownBy(() -> userService.signUp(signUpRequest))
                .isInstanceOf(DuplicateLoginIdException.class)
                .hasFieldOrPropertyWithValue("errorCode", "U-02")
                .hasFieldOrPropertyWithValue("errorMessage", "이미 존재하는 로그인 아이디입니다.");

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
        assertThatThrownBy(() -> userService.signUp(signUpRequest))
                .isInstanceOf(DuplicateNicknameException.class)
                .hasFieldOrPropertyWithValue("errorCode", "U-03")
                .hasFieldOrPropertyWithValue("errorMessage", "이미 존재하는 닉네임입니다.");

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
        assertThatThrownBy(() -> userService.signUp(signUpRequest))
                .isInstanceOf(DuplicateEmailException.class)
                .hasFieldOrPropertyWithValue("errorCode", "U-04")
                .hasFieldOrPropertyWithValue("errorMessage", "이미 존재하는 이메일입니다.");

        // Then
        verify(userRepository).findByEmail(signUpRequest.getEmail());
    }

    @Test
    @DisplayName("로그인 아이디를 통해 사용자에게 USER 권한을 부여한다")
    void authorizeUser() {
        // Given
        String loginId = "loginId";
        User user = User.builder()
                .loginId(loginId)
                .role(Role.GUEST)
                .build();

        given(userRepository.findByLoginId(loginId)).willReturn(Optional.of(user));
        given(userRepository.save(any(User.class))).will(returnsFirstArg());

        // When
        User authorizedUser = userService.grantUserRole(loginId);

        // Then
        assertThat(authorizedUser.getRole()).isEqualTo(Role.USER);

        verify(userRepository).findByLoginId(loginId);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자에게 USER 권한을 부여할 때 로그인 아이디와 일치하는 사용자가 없으면 예외를 던진다")
    void usernameNotFoundWhenAuthorizingUser() {
        // Given
        String loginId = "loginId";

        given(userRepository.findByLoginId(loginId)).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> userService.grantUserRole(loginId))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(loginId);

        // Then
        verify(userRepository).findByLoginId(loginId);
    }

}