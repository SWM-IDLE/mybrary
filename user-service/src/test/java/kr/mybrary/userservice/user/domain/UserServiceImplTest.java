package kr.mybrary.userservice.user.domain;

import kr.mybrary.userservice.global.util.MultipartFileUtil;
import kr.mybrary.userservice.user.UserFixture;
import kr.mybrary.userservice.user.UserTestData;
import kr.mybrary.userservice.user.domain.dto.request.*;
import kr.mybrary.userservice.user.domain.dto.response.*;
import kr.mybrary.userservice.user.domain.exception.io.EmptyFileException;
import kr.mybrary.userservice.user.domain.exception.profile.ProfileImageFileSizeExceededException;
import kr.mybrary.userservice.user.domain.exception.profile.ProfileImageUrlNotFoundException;
import kr.mybrary.userservice.user.domain.exception.user.DuplicateLoginIdException;
import kr.mybrary.userservice.user.domain.exception.user.DuplicateNicknameException;
import kr.mybrary.userservice.user.domain.exception.user.UserNotFoundException;
import kr.mybrary.userservice.user.domain.storage.StorageService;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    StorageService storageService;
    @InjectMocks
    UserServiceImpl userService;

    static final String LOGIN_ID = "loginId";
    static final String FOLLOWING_ID = "followingId";
    static final String FOLLOWER_ID = "followerId";
    static final String PROFILE_IMAGE_PATH = "profile/profileImage/";
    static final String PROFILE_IMAGE_BASE_URL = "https://mybrary-user-service.s3.ap-northeast-2.amazonaws.com/profile/profileImage/";

    @Test
    @DisplayName("회원가입 요청이 들어오면 암호화된 비밀번호와 함께 회원 권한으로 사용자 정보를 저장한다")
    void signUp() {
        // Given
        SignUpServiceRequest serviceRequest = UserTestData.createSignUpServiceRequest();

        given(userRepository.existsByLoginId(serviceRequest.getLoginId())).willReturn(false);
        given(userRepository.existsByNickname(serviceRequest.getNickname())).willReturn(false);
        given(passwordEncoder.encode(serviceRequest.getPassword())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(UserFixture.COMMON_USER.getUser());

        // When
        SignUpServiceResponse savedUser = userService.signUp(serviceRequest);

        // Then
        assertAll(
                () -> assertThat(savedUser.getLoginId()).isEqualTo(serviceRequest.getLoginId()),
                () -> assertThat(savedUser.getNickname()).isEqualTo(serviceRequest.getNickname()),
                () -> assertThat(savedUser.getEmail()).isEqualTo(serviceRequest.getEmail()),
                () -> assertThat(savedUser.getRole()).isEqualTo(Role.USER)
        );

        verify(userRepository).existsByLoginId(serviceRequest.getLoginId());
        verify(userRepository).existsByNickname(serviceRequest.getNickname());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("아이디가 중복되면 예외를 던진다")
    void signUpWithDuplicateLoginId() {
        // Given
        SignUpServiceRequest serviceRequest = UserTestData.createSignUpServiceRequest();

        given(userRepository.existsByLoginId(serviceRequest.getLoginId())).willReturn(true);

        // When
        assertThatThrownBy(() -> userService.signUp(serviceRequest))
                .isInstanceOf(DuplicateLoginIdException.class)
                .hasFieldOrPropertyWithValue("status", 400)
                .hasFieldOrPropertyWithValue("errorCode", "U-02")
                .hasFieldOrPropertyWithValue("errorMessage", "이미 존재하는 로그인 아이디입니다.");

        // Then
        verify(userRepository).existsByLoginId(serviceRequest.getLoginId());
    }

    @Test
    @DisplayName("닉네임이 중복되면 예외를 던진다")
    void signUpWithDuplicateNickname() {
        // Given
        SignUpServiceRequest serviceRequest = UserTestData.createSignUpServiceRequest();

        given(userRepository.existsByNickname(serviceRequest.getNickname())).willReturn(true);

        // When
        assertThatThrownBy(() -> userService.signUp(serviceRequest))
                .isInstanceOf(DuplicateNicknameException.class)
                .hasFieldOrPropertyWithValue("status", 400)
                .hasFieldOrPropertyWithValue("errorCode", "U-03")
                .hasFieldOrPropertyWithValue("errorMessage", "이미 존재하는 닉네임입니다.");

        // Then
        verify(userRepository).existsByNickname(serviceRequest.getNickname());
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 정보를 조회한다")
    void getUserProfile() {
        // Given
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(
                Optional.of(UserFixture.COMMON_USER.getUser()));

        // When
        ProfileServiceResponse userProfile = userService.getProfile(LOGIN_ID);

        // Then
        assertAll(
                () -> assertThat(userProfile.getNickname()).isEqualTo(
                        UserFixture.COMMON_USER.getUser().getNickname()),
                () -> assertThat(userProfile.getIntroduction()).isEqualTo(
                        UserFixture.COMMON_USER.getUser().getIntroduction()),
                () -> assertThat(userProfile.getProfileImageUrl()).isEqualTo(
                        UserFixture.COMMON_USER.getUser().getProfileImageUrl())
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
                .hasFieldOrPropertyWithValue("status", 404)
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
                Optional.of(UserFixture.COMMON_USER.getUser()));

        // When
        ProfileImageUrlServiceResponse profileImage = userService.getProfileImageUrl(LOGIN_ID);

        // Then
        assertThat(profileImage.getProfileImageUrl()).isEqualTo(
                UserFixture.COMMON_USER.getUser().getProfileImageUrl());

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
                .hasFieldOrPropertyWithValue("status", 404)
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
                Optional.of(UserFixture.USER_WITHOUT_PROFILE_IMAGE_URL.getUser()));

        // When
        assertThatThrownBy(() -> userService.getProfileImageUrl(LOGIN_ID))
                .isInstanceOf(ProfileImageUrlNotFoundException.class)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue("errorCode", "P-01")
                .hasFieldOrPropertyWithValue("errorMessage", "프로필 이미지 URL이 존재하지 않습니다.");

        // Then
        verify(userRepository).findByLoginId(LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 정보를 수정할 때 사용자가 없으면 예외를 던진다")
    void updateProfileWithNotExistUser() {
        // Given
        ProfileUpdateServiceRequest serviceRequest = UserTestData.createProfileUpdateServiceRequest();
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> userService.updateProfile(serviceRequest))
                .isInstanceOf(UserNotFoundException.class)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue("errorCode", "U-05")
                .hasFieldOrPropertyWithValue("errorMessage", "존재하지 않는 사용자입니다.");

        // Then
        verify(userRepository).findByLoginId(LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 정보를 수정할 때 닉네임이 중복되면 예외를 던진다")
    void updateProfileWithDuplicateNickname() {
        // Given
        ProfileUpdateServiceRequest serviceRequest = UserTestData.createProfileUpdateServiceRequest();
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.of(UserFixture.COMMON_USER.getUser()));
        given(userRepository.existsByNickname(serviceRequest.getNickname())).willReturn(true);

        // When
        assertThatThrownBy(() -> userService.updateProfile(serviceRequest))
                .isInstanceOf(DuplicateNicknameException.class)
                .hasFieldOrPropertyWithValue("status", 400)
                .hasFieldOrPropertyWithValue("errorCode", "U-03")
                .hasFieldOrPropertyWithValue("errorMessage", "이미 존재하는 닉네임입니다.");

        // Then
        verify(userRepository).existsByNickname(serviceRequest.getNickname());
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지를 수정한다")
    void updateProfileImage() throws Exception {
        // Given
        ProfileImageUpdateServiceRequest serviceRequest = UserTestData.createProfileImageUpdateServiceRequest();
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(
                Optional.of(UserFixture.COMMON_USER.getUser()));
        given(storageService.putFile(serviceRequest.getProfileImage(),
                MultipartFileUtil.generateFilePath(PROFILE_IMAGE_PATH, LOGIN_ID, serviceRequest.getProfileImage())))
                .willReturn(PROFILE_IMAGE_BASE_URL + LOGIN_ID);

        // When
        ProfileImageUrlServiceResponse updatedProfileImage = userService.updateProfileImage(
                serviceRequest);

        // Then
        assertAll(
                () -> assertThat(updatedProfileImage.getProfileImageUrl()).isEqualTo(
                        PROFILE_IMAGE_BASE_URL + LOGIN_ID)
        );

        verify(userRepository).findByLoginId(LOGIN_ID);
        verify(storageService).putFile(serviceRequest.getProfileImage(),
                MultipartFileUtil.generateFilePath(PROFILE_IMAGE_PATH, LOGIN_ID, serviceRequest.getProfileImage()));
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지를 수정할 때 사용자가 없으면 예외를 던진다")
    void updateProfileImageWithNotExistUser() throws Exception {
        // Given
        ProfileImageUpdateServiceRequest serviceRequest = UserTestData.createProfileImageUpdateServiceRequest();
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> userService.updateProfileImage(serviceRequest))
                .isInstanceOf(UserNotFoundException.class)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue("errorCode", "U-05")
                .hasFieldOrPropertyWithValue("errorMessage", "존재하지 않는 사용자입니다.");

        // Then
        verify(userRepository).findByLoginId(LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지를 수정할 때 프로필 이미지가 없으면 예외를 던진다")
    void updateProfileImageWithNoProfileImage() {
        // Given
        ProfileImageUpdateServiceRequest serviceRequest = UserTestData.createProfileImageUpdateServiceRequestWithEmptyFile();

        // When Then
        assertThatThrownBy(() -> userService.updateProfileImage(serviceRequest))
                .isInstanceOf(EmptyFileException.class)
                .hasFieldOrPropertyWithValue("status", 400)
                .hasFieldOrPropertyWithValue("errorCode", "IO-01")
                .hasFieldOrPropertyWithValue("errorMessage", "파일이 비어있습니다. 파일을 선택해주세요.");
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지를 수정할 때 프로필 이미지가 5MB를 초과하면 예외를 던진다")
    void updateProfileImageWithProfileImageSizeExceed() throws Exception {
        // Given
        ProfileImageUpdateServiceRequest serviceRequest = UserTestData.createProfileImageUpdateServiceRequestOver5MB();

        // When Then
        assertThatThrownBy(() -> userService.updateProfileImage(serviceRequest))
                .isInstanceOf(ProfileImageFileSizeExceededException.class)
                .hasFieldOrPropertyWithValue("status", 400)
                .hasFieldOrPropertyWithValue("errorCode", "P-02")
                .hasFieldOrPropertyWithValue("errorMessage",
                        "프로필 이미지 파일의 크기가 너무 큽니다. 최대 5MB까지 업로드 가능합니다.");
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지를 삭제한다(기본 프로필 이미지로 대체한다)")
    void deleteProfileImage() {
        // Given
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(
                Optional.of(UserFixture.COMMON_USER.getUser()));

        // When
        ProfileImageUrlServiceResponse updatedProfileImage = userService.deleteProfileImage(
                LOGIN_ID);

        // Then
        assertAll(
                () -> assertThat(updatedProfileImage.getProfileImageUrl()).isEqualTo(
                        PROFILE_IMAGE_BASE_URL + "default.jpg")
        );

        verify(userRepository).findByLoginId(LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지를 삭제할 때 사용자가 없으면 예외를 던진다")
    void deleteProfileImageWithNotExistUser() {
        // Given
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> userService.deleteProfileImage(LOGIN_ID))
                .isInstanceOf(UserNotFoundException.class)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue("errorCode", "U-05")
                .hasFieldOrPropertyWithValue("errorMessage", "존재하지 않는 사용자입니다.");

        // Then
        verify(userRepository).findByLoginId(LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자의 팔로워 목록을 조회한다")
    void getFollowers() {
        // Given
        given(userRepository.findByLoginId(FOLLOWING_ID)).willReturn(Optional.of(UserFixture.USER_WITH_FOLLOWER.getUser()));

        // When
        FollowerServiceResponse followerServiceResponse = userService.getFollowers(FOLLOWING_ID);

        // Then
        assertAll(
                () -> assertThat(followerServiceResponse.getRequestLoginId()).isEqualTo(FOLLOWING_ID),
                () -> assertThat(followerServiceResponse.getFollowers()).hasSize(1),
                () -> assertThat(followerServiceResponse.getFollowers()).extracting("loginId").containsExactly(FOLLOWER_ID)
        );

        verify(userRepository).findByLoginId(FOLLOWING_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자의 팔로워 목록을 조회할 때 사용자가 없으면 예외를 던진다")
    void getFollowersWithNotExistUser() {
        // Given
        given(userRepository.findByLoginId(FOLLOWING_ID)).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> userService.getFollowers(FOLLOWING_ID))
                .isInstanceOf(UserNotFoundException.class)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue("errorCode", "U-05")
                .hasFieldOrPropertyWithValue("errorMessage", "존재하지 않는 사용자입니다.");

        // Then
        verify(userRepository).findByLoginId(FOLLOWING_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자의 팔로잉 목록을 조회한다")
    void getFollowings() {
        // Given
        given(userRepository.findByLoginId(FOLLOWER_ID)).willReturn(Optional.of(UserFixture.USER_AS_FOLLOWER.getUser()));

        // When
        FollowingServiceResponse followingServiceResponse = userService.getFollowings(FOLLOWER_ID);

        // Then
        assertAll(
                () -> assertThat(followingServiceResponse.getRequestLoginId()).isEqualTo(FOLLOWER_ID),
                () -> assertThat(followingServiceResponse.getFollowings()).hasSize(1),
                () -> assertThat(followingServiceResponse.getFollowings()).extracting("loginId").containsExactly(FOLLOWING_ID)
        );

        verify(userRepository).findByLoginId(FOLLOWER_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자의 팔로잉 목록을 조회할 때 사용자가 없으면 예외를 던진다")
    void getFollowingsWithNotExistUser() {
        // Given
        given(userRepository.findByLoginId(FOLLOWER_ID)).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> userService.getFollowings(FOLLOWER_ID))
                .isInstanceOf(UserNotFoundException.class)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue("errorCode", "U-05")
                .hasFieldOrPropertyWithValue("errorMessage", "존재하지 않는 사용자입니다.");

        // Then
        verify(userRepository).findByLoginId(FOLLOWER_ID);
    }

}