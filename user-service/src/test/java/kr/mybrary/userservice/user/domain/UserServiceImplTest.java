package kr.mybrary.userservice.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import kr.mybrary.userservice.user.UserTestData;
import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.userservice.user.domain.dto.response.ProfileImageUrlServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.ProfileServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.SignUpServiceResponse;
import kr.mybrary.userservice.user.domain.exception.DuplicateEmailException;
import kr.mybrary.userservice.user.domain.exception.DuplicateLoginIdException;
import kr.mybrary.userservice.user.domain.exception.DuplicateNicknameException;
import kr.mybrary.userservice.user.domain.exception.ProfileImageUrlNotFoundException;
import kr.mybrary.userservice.user.domain.exception.UserNotFoundException;
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
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserServiceImpl userService;

    static final String LOGIN_ID = "loginId";

    @Test
    @DisplayName("회원가입 요청이 들어오면 암호화된 비밀번호와 함께 회원 권한으로 사용자 정보를 저장한다")
    void signUp() {
        // Given
        SignUpServiceRequest serviceRequest = UserTestData.createSignUpServiceRequest();

        given(userRepository.findByLoginId(serviceRequest.getLoginId())).willReturn(Optional.empty());
        given(userRepository.findByNickname(serviceRequest.getNickname())).willReturn(Optional.empty());
        given(userRepository.findByEmail(serviceRequest.getEmail())).willReturn(Optional.empty());
        given(passwordEncoder.encode(serviceRequest.getPassword())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(UserTestData.createUser());

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
    void signUpWithDuplicateLoginId() {
        // Given
        SignUpServiceRequest serviceRequest = UserTestData.createSignUpServiceRequest();

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
    void signUpWithDuplicateNickname() {
        // Given
        SignUpServiceRequest serviceRequest = UserTestData.createSignUpServiceRequest();

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
    void signUpWithDuplicateEmail() {
        // Given
        SignUpServiceRequest serviceRequest = UserTestData.createSignUpServiceRequest();

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

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 정보를 조회한다")
    void getUserProfile() {
        // Given
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(
                Optional.of(UserTestData.createUser()));

        // When
        ProfileServiceResponse userProfile = userService.getProfile(LOGIN_ID);

        // Then
        Assertions.assertAll(
                () -> assertThat(userProfile.getNickname()).isEqualTo("nickname"),
                () -> assertThat(userProfile.getEmail()).isEqualTo("email@mail.com"),
                () -> assertThat(userProfile.getIntroduction()).isEqualTo("introduction"),
                () -> assertThat(userProfile.getProfileImageUrl()).isEqualTo("profileImageUrl")
        );

        verify(userRepository).findByLoginId(LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 정보를 조회할 때 사용자가 없으면 예외를 던진다")
    void getUserProfileWithNotExistUser() {
        // Given
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> userService.getProfile(LOGIN_ID))
                .isInstanceOf(UserNotFoundException.class)
                .hasFieldOrPropertyWithValue("errorCode", "U-05")
                .hasFieldOrPropertyWithValue("errorMessage", "존재하지 않는 사용자입니다.");

        // Then
        verify(userRepository).findByLoginId(LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지 URL을 조회한다")
    void getProfileImageUrl() {
        // Given
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(
                Optional.of(UserTestData.createUser()));

        // When
        ProfileImageUrlServiceResponse profileImage = userService.getProfileImageUrl(LOGIN_ID);

        // Then
        assertThat(profileImage.getProfileImageUrl()).isEqualTo("profileImageUrl");

        verify(userRepository).findByLoginId(LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지 URL을 조회할 때 사용자가 없으면 예외를 던진다")
    void getProfileImageUrlWithNotExistUser() {
        // Given
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> userService.getProfileImageUrl(LOGIN_ID))
                .isInstanceOf(UserNotFoundException.class)
                .hasFieldOrPropertyWithValue("errorCode", "U-05")
                .hasFieldOrPropertyWithValue("errorMessage", "존재하지 않는 사용자입니다.");

        // Then
        verify(userRepository).findByLoginId(LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지 URL을 조회할 때 프로필 이미지 URL이 없으면 예외를 던진다")
    void getProfileImageUrlWithNoProfileImageUrl() {
        // Given
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(
                Optional.of(UserTestData.createUserWithOutProfileImageUrl()));

        // When
        assertThatThrownBy(() -> userService.getProfileImageUrl(LOGIN_ID))
                .isInstanceOf(ProfileImageUrlNotFoundException.class)
                .hasFieldOrPropertyWithValue("errorCode", "P-01")
                .hasFieldOrPropertyWithValue("errorMessage", "프로필 이미지 URL이 존재하지 않습니다.");

        // Then
        verify(userRepository).findByLoginId(LOGIN_ID);
    }
}