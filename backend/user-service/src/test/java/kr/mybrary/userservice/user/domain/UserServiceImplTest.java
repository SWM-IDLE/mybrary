package kr.mybrary.userservice.user.domain;

import kr.mybrary.userservice.user.UserFixture;
import kr.mybrary.userservice.user.UserTestData;
import kr.mybrary.userservice.user.domain.dto.request.*;
import kr.mybrary.userservice.user.domain.dto.response.*;
import kr.mybrary.userservice.user.domain.exception.io.EmptyFileException;
import kr.mybrary.userservice.user.domain.exception.profile.ProfileImageFileSizeExceededException;
import kr.mybrary.userservice.user.domain.exception.profile.ProfileImageSizeOptionNotSupportedException;
import kr.mybrary.userservice.user.domain.exception.profile.ProfileImageUrlNotFoundException;
import kr.mybrary.userservice.user.domain.exception.profile.ProfileUpdateRequestNotAuthenticated;
import kr.mybrary.userservice.user.domain.exception.user.DuplicateLoginIdException;
import kr.mybrary.userservice.user.domain.exception.user.DuplicateNicknameException;
import kr.mybrary.userservice.user.domain.exception.user.UserNotFoundException;
import kr.mybrary.userservice.user.domain.storage.StorageService;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.persistence.model.FollowUserInfoModel;
import kr.mybrary.userservice.user.persistence.model.UserInfoModel;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
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
    @DisplayName("로그인 아이디로 사용자를 가져와 반환한다")
    void getUserResponseByLoginId() {
        // Given
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(
                Optional.of(UserFixture.COMMON_USER.getUser()));

        // When
        UserResponse userResponse = userService.getUserResponse(LOGIN_ID);

        // Then
        assertAll(
                () -> assertThat(userResponse.getUser().getLoginId()).isEqualTo(
                        UserFixture.COMMON_USER.getUser().getLoginId()),
                () -> assertThat(userResponse.getUser().getNickname()).isEqualTo(
                        UserFixture.COMMON_USER.getUser().getNickname()),
                () -> assertThat(userResponse.getUser().getEmail()).isEqualTo(
                        UserFixture.COMMON_USER.getUser().getEmail()),
                () -> assertThat(userResponse.getUser().getRole()).isEqualTo(
                        UserFixture.COMMON_USER.getUser().getRole())
        );

        verify(userRepository).findByLoginId(LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자를 가져올 때 사용자가 없으면 예외를 던진다")
    void getUserResponseByLoginIdWithNotExistUser() {
        // Given
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> userService.getUserResponse(LOGIN_ID))
                .isInstanceOf(UserNotFoundException.class)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue("errorCode", "U-05")
                .hasFieldOrPropertyWithValue("errorMessage", "존재하지 않는 사용자입니다.");

        // Then
        verify(userRepository).findByLoginId(LOGIN_ID);
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
    @DisplayName("로그인 아이디로 사용자 프로필 이미지 원본 URL을 조회한다")
    void getProfileImageUrl() {
        // Given
        ProfileImageUrlServiceRequest serviceRequest = ProfileImageUrlServiceRequest.builder()
                .userId(LOGIN_ID)
                .size("original")
                .build();

        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.of(UserFixture.COMMON_USER.getUser()));

        // When
        ProfileImageUrlServiceResponse profileImage = userService.getProfileImageUrl(serviceRequest);

        // Then
        assertThat(profileImage.getProfileImageUrl()).isEqualTo(
                UserFixture.COMMON_USER.getUser().getProfileImageUrl());

        verify(userRepository).findByLoginId(LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지 원본 URL을 조회할 때 사용자가 없으면 예외를 던진다")
    void getProfileImageUrlWithNotExistUser() {
        // Given
        ProfileImageUrlServiceRequest serviceRequest = ProfileImageUrlServiceRequest.builder()
                .userId(LOGIN_ID)
                .size("original")
                .build();

        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> userService.getProfileImageUrl(serviceRequest))
                .isInstanceOf(UserNotFoundException.class)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue("errorCode", "U-05")
                .hasFieldOrPropertyWithValue("errorMessage", "존재하지 않는 사용자입니다.");

        // Then
        verify(userRepository).findByLoginId(LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지 원본 URL을 조회할 때 프로필 이미지 원본 URL이 없으면 예외를 던진다")
    void getProfileImageUrlWithNoProfileImageUrl() {
        // Given
        ProfileImageUrlServiceRequest serviceRequest = ProfileImageUrlServiceRequest.builder()
                .userId(LOGIN_ID)
                .size("original")
                .build();

        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(
                Optional.of(UserFixture.USER_WITHOUT_PROFILE_IMAGE_URL.getUser()));

        // When
        assertThatThrownBy(() -> userService.getProfileImageUrl(serviceRequest))
                .isInstanceOf(ProfileImageUrlNotFoundException.class)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue("errorCode", "P-01")
                .hasFieldOrPropertyWithValue("errorMessage", "프로필 이미지 URL이 존재하지 않습니다.");

        // Then
        verify(userRepository).findByLoginId(LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지 썸네일 (tiny) URL을 조회한다. 썸네일 URL이 업데이트 되어 있다면 바로 반환한다.")
    void getProfileImagThumbnailTinyUrl() {
        // Given
        ProfileImageUrlServiceRequest serviceRequest = ProfileImageUrlServiceRequest.builder()
                .userId(LOGIN_ID)
                .size("tiny")
                .build();

        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.of(UserFixture.COMMON_USER.getUser()));
        given(storageService.getPathFromUrl("profileImageThumbnailTinyUrl")).willReturn("tiny-profileImagePath");
        given(storageService.getPathFromUrl("profileImageUrl")).willReturn("profileImagePath");

        // When
        ProfileImageUrlServiceResponse profileImage = userService.getProfileImageUrl(serviceRequest);

        // Then
        assertThat(profileImage.getProfileImageUrl()).isEqualTo(UserFixture.COMMON_USER.getUser().getProfileImageThumbnailTinyUrl());

        verify(userRepository).findByLoginId(LOGIN_ID);
        verify(storageService).getPathFromUrl("profileImageThumbnailTinyUrl");
        verify(storageService).getPathFromUrl("profileImageUrl");
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지 썸네일 (small) URL을 조회한다. 썸네일 URL이 업데이트 되어 있다면 바로 반환한다.")
    void getProfileImagThumbnailSmallUrl() {
        // Given
        ProfileImageUrlServiceRequest serviceRequest = ProfileImageUrlServiceRequest.builder()
                .userId(LOGIN_ID)
                .size("small")
                .build();

        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.of(UserFixture.COMMON_USER.getUser()));
        given(storageService.getPathFromUrl("profileImageThumbnailSmallUrl")).willReturn("small-profileImagePath");
        given(storageService.getPathFromUrl("profileImageUrl")).willReturn("profileImagePath");

        // When
        ProfileImageUrlServiceResponse profileImage = userService.getProfileImageUrl(serviceRequest);

        // Then
        assertThat(profileImage.getProfileImageUrl()).isEqualTo(UserFixture.COMMON_USER.getUser().getProfileImageThumbnailSmallUrl());

        verify(userRepository).findByLoginId(LOGIN_ID);
        verify(storageService).getPathFromUrl("profileImageThumbnailSmallUrl");
        verify(storageService).getPathFromUrl("profileImageUrl");
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지 썸네일 URL을 조회한다. 지원하지 않는 썸네일 사이즈 옵션이라면 예외를 던진다.")
    void getProfileImagThumbnailNotSupportedSizeUrl() {
        // Given
        ProfileImageUrlServiceRequest serviceRequest = ProfileImageUrlServiceRequest.builder()
                .userId(LOGIN_ID)
                .size("notSupportedSize")
                .build();

        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.of(UserFixture.COMMON_USER.getUser()));

        // When
        assertThatThrownBy(() -> userService.getProfileImageUrl(serviceRequest))
                .isInstanceOf(ProfileImageSizeOptionNotSupportedException.class)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue("errorCode", "P-04")
                .hasFieldOrPropertyWithValue("errorMessage", "지원되지 않는 프로필 이미지 크기 옵션입니다. 현재 지원하는 옵션은 tiny, small, original 입니다.");

        verify(userRepository).findByLoginId(LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지 썸네일 (tiny) URL을 조회한다." +
            "썸네일 URL이 업데이트 되어 있지 않다면 스토리지에서 조회한 뒤, 썸네일 파일이 있다면 URL을 업데이트 하여 반환한다.")
    void getProfileImagThumbnailTinyUrlNotUpdated() {
        // Given
        ProfileImageUrlServiceRequest serviceRequest = ProfileImageUrlServiceRequest.builder()
                .userId(LOGIN_ID)
                .size("tiny")
                .build();

        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.of(UserFixture.COMMON_USER.getUser()));
        given(storageService.getPathFromUrl("profileImageThumbnailTinyUrl")).willReturn("tiny-profileImagePath-notUpdated");
        given(storageService.getPathFromUrl("profileImageUrl")).willReturn("profileImagePath");
        given(storageService.hasResizedFiles("profileImagePath", "tiny")).willReturn(true);
        given(storageService.getResizedUrl("profileImageUrl", "tiny")).willReturn("profileImageThumbnailTinyUrl");

        // When
        ProfileImageUrlServiceResponse profileImage = userService.getProfileImageUrl(serviceRequest);

        // Then
        assertThat(profileImage.getProfileImageUrl()).isEqualTo(UserFixture.COMMON_USER.getUser().getProfileImageThumbnailTinyUrl());

        verify(userRepository).findByLoginId(LOGIN_ID);
        verify(storageService).getPathFromUrl("profileImageThumbnailTinyUrl");
        verify(storageService, times(2)).getPathFromUrl("profileImageUrl");
        verify(storageService).hasResizedFiles("profileImagePath", "tiny");
        verify(storageService).getResizedUrl("profileImageUrl", "tiny");
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지 썸네일 (small) URL을 조회한다." +
            "썸네일 URL이 업데이트 되어 있지 않다면 스토리지에서 조회한 뒤, 썸네일 파일이 있다면 URL을 업데이트 하여 반환한다.")
    void getProfileImagThumbnailSmallUrlNotUpdated() {
        // Given
        ProfileImageUrlServiceRequest serviceRequest = ProfileImageUrlServiceRequest.builder()
                .userId(LOGIN_ID)
                .size("small")
                .build();

        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.of(UserFixture.COMMON_USER.getUser()));
        given(storageService.getPathFromUrl("profileImageThumbnailSmallUrl")).willReturn("small-profileImagePath-notUpdated");
        given(storageService.getPathFromUrl("profileImageUrl")).willReturn("profileImagePath");
        given(storageService.hasResizedFiles("profileImagePath", "small")).willReturn(true);
        given(storageService.getResizedUrl("profileImageUrl", "small")).willReturn("profileImageThumbnailSmallUrl");

        // When
        ProfileImageUrlServiceResponse profileImage = userService.getProfileImageUrl(serviceRequest);

        // Then
        assertThat(profileImage.getProfileImageUrl()).isEqualTo(UserFixture.COMMON_USER.getUser().getProfileImageThumbnailSmallUrl());

        verify(userRepository).findByLoginId(LOGIN_ID);
        verify(storageService).getPathFromUrl("profileImageThumbnailSmallUrl");
        verify(storageService, times(2)).getPathFromUrl("profileImageUrl");
        verify(storageService).hasResizedFiles("profileImagePath", "small");
        verify(storageService).getResizedUrl("profileImageUrl", "small");
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지 썸네일 (tiny) URL을 조회한다." +
            "썸네일 URL이 업데이트 되어 있지 않다면 스토리지에서 조회한 뒤, 썸네일 파일이 없다면 원본 URL을 반환한다.")
    void getProfileImagThumbnailTinyUrlNotUpdatedNotStored() {
        // Given
        ProfileImageUrlServiceRequest serviceRequest = ProfileImageUrlServiceRequest.builder()
                .userId(LOGIN_ID)
                .size("tiny")
                .build();

        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.of(UserFixture.COMMON_USER.getUser()));
        given(storageService.getPathFromUrl("profileImageThumbnailTinyUrl")).willReturn("tiny-profileImagePath-notUpdated");
        given(storageService.getPathFromUrl("profileImageUrl")).willReturn("profileImagePath");
        given(storageService.hasResizedFiles("profileImagePath", "tiny")).willReturn(false);

        // When
        ProfileImageUrlServiceResponse profileImage = userService.getProfileImageUrl(serviceRequest);

        // Then
        assertThat(profileImage.getProfileImageUrl()).isEqualTo(UserFixture.COMMON_USER.getUser().getProfileImageUrl());

        verify(userRepository).findByLoginId(LOGIN_ID);
        verify(storageService).getPathFromUrl("profileImageThumbnailTinyUrl");
        verify(storageService, times(2)).getPathFromUrl("profileImageUrl");
        verify(storageService).hasResizedFiles("profileImagePath", "tiny");
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지 썸네일 (small) URL을 조회한다." +
            "썸네일 URL이 업데이트 되어 있지 않다면 스토리지에서 조회한 뒤, 썸네일 파일이 없다면 원본 URL을 반환한다.")
    void getProfileImagThumbnailSmallUrlNotUpdatedNotStored() {
        // Given
        ProfileImageUrlServiceRequest serviceRequest = ProfileImageUrlServiceRequest.builder()
                .userId(LOGIN_ID)
                .size("small")
                .build();

        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.of(UserFixture.COMMON_USER.getUser()));
        given(storageService.getPathFromUrl("profileImageThumbnailSmallUrl")).willReturn("small-profileImagePath-notUpdated");
        given(storageService.getPathFromUrl("profileImageUrl")).willReturn("profileImagePath");
        given(storageService.hasResizedFiles("profileImagePath", "small")).willReturn(false);

        // When
        ProfileImageUrlServiceResponse profileImage = userService.getProfileImageUrl(serviceRequest);

        // Then
        assertThat(profileImage.getProfileImageUrl()).isEqualTo(UserFixture.COMMON_USER.getUser().getProfileImageUrl());

        verify(userRepository).findByLoginId(LOGIN_ID);
        verify(storageService).getPathFromUrl("profileImageThumbnailSmallUrl");
        verify(storageService, times(2)).getPathFromUrl("profileImageUrl");
        verify(storageService).hasResizedFiles("profileImagePath", "small");
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
    @DisplayName("로그인 아이디로 사용자 프로필 정보를 수정할 때 로그인한 사용자와 수정할 사용자가 다르면 예외를 던진다")
    void updateProfileWithDifferentUser() {
        // Given
        ProfileUpdateServiceRequest serviceRequest = ProfileUpdateServiceRequest.builder()
                .userId(LOGIN_ID)
                .loginId("another")
                .nickname("newNickname")
                .introduction("newIntroduction")
                .build();

        // When Then
        assertThatThrownBy(() -> userService.updateProfile(serviceRequest))
                .isInstanceOf(ProfileUpdateRequestNotAuthenticated.class)
                .hasFieldOrPropertyWithValue("status", 403)
                .hasFieldOrPropertyWithValue("errorCode", "P-03")
                .hasFieldOrPropertyWithValue("errorMessage", "프로필을 수정할 수 있는 권한이 없습니다. 로그인한 사용자와 프로필 수정을 요청한 사용자가 일치하지 않습니다.");
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
        ProfileImageUpdateServiceRequest serviceRequest = ProfileImageUpdateServiceRequest.builder()
                .userId(LOGIN_ID)
                .loginId(LOGIN_ID)
                .profileImage(UserTestData.createMockMultipartFile())
                .localDateTime(LocalDateTime.of(2023, 1, 1, 0, 0, 0, 123000000))
                .build();
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.of(UserFixture.COMMON_USER.getUser()));
        given(storageService.putFile(serviceRequest.getProfileImage(), "profile/profileImage/loginId/2023-01-01_00-00-00-123.jpg"))
                .willReturn(PROFILE_IMAGE_BASE_URL + LOGIN_ID);

        // When
        ProfileImageUrlServiceResponse updatedProfileImage = userService.updateProfileImage(serviceRequest);

        // Then
        assertAll(
                () -> assertThat(updatedProfileImage.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_BASE_URL + LOGIN_ID)
        );

        verify(userRepository).findByLoginId(LOGIN_ID);
        verify(storageService).putFile(serviceRequest.getProfileImage(), "profile/profileImage/loginId/2023-01-01_00-00-00-123.jpg");
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
        ProfileImageUpdateServiceRequest serviceRequest = ProfileImageUpdateServiceRequest.builder()
                .userId(LOGIN_ID)
                .loginId(LOGIN_ID)
                .build();

        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(
                Optional.of(UserFixture.COMMON_USER.getUser()));

        // When
        ProfileImageUrlServiceResponse updatedProfileImage = userService.deleteProfileImage(serviceRequest);

        // Then
        assertAll(
                () -> assertThat(updatedProfileImage.getProfileImageUrl()).isEqualTo(
                        PROFILE_IMAGE_BASE_URL + "default.jpg")
        );

        verify(userRepository).findByLoginId(LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지를 삭제할 때 로그인한 사용자와 삭제할 사용자가 다르면 예외를 던진다")
    void deleteProfileImageWithDifferentUser() {
        // Given
        ProfileImageUpdateServiceRequest serviceRequest = ProfileImageUpdateServiceRequest.builder()
                .userId(LOGIN_ID)
                .loginId("another")
                .build();

        // When Then
        assertThatThrownBy(() -> userService.deleteProfileImage(serviceRequest))
                .isInstanceOf(ProfileUpdateRequestNotAuthenticated.class)
                .hasFieldOrPropertyWithValue("status", 403)
                .hasFieldOrPropertyWithValue("errorCode", "P-03")
                .hasFieldOrPropertyWithValue("errorMessage", "프로필을 수정할 수 있는 권한이 없습니다. 로그인한 사용자와 프로필 수정을 요청한 사용자가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 프로필 이미지를 삭제할 때 사용자가 없으면 예외를 던진다")
    void deleteProfileImageWithNotExistUser() {
        // Given
        ProfileImageUpdateServiceRequest serviceRequest = ProfileImageUpdateServiceRequest.builder()
                .userId(LOGIN_ID)
                .loginId(LOGIN_ID)
                .build();

        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> userService.deleteProfileImage(serviceRequest))
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

        FollowUserInfoModel followerUser = FollowUserInfoModel.builder()
                .loginId(FOLLOWER_ID)
                .nickname("followerNickname")
                .profileImageUrl("followerProfileImageUrl")
                .build();

        given(userRepository.findAllFollowers(anyLong())).willReturn(Arrays.asList(followerUser));

        // When
        FollowerServiceResponse followerServiceResponse = userService.getFollowers(FOLLOWING_ID);

        // Then
        assertAll(
                () -> assertThat(followerServiceResponse.getUserId()).isEqualTo(FOLLOWING_ID),
                () -> assertThat(followerServiceResponse.getFollowers()).hasSize(1),
                () -> assertThat(followerServiceResponse.getFollowers()).extracting("userId").containsExactly(FOLLOWER_ID),
                () -> assertThat(followerServiceResponse.getFollowers()).extracting("nickname").containsExactly(followerUser.getNickname()),
                () -> assertThat(followerServiceResponse.getFollowers()).extracting("profileImageUrl").containsExactly(followerUser.getProfileImageUrl())
        );

        verify(userRepository).findByLoginId(FOLLOWING_ID);
        verify(userRepository).findAllFollowers(anyLong());
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

        FollowUserInfoModel followingUser = FollowUserInfoModel.builder()
                .loginId(FOLLOWING_ID)
                .nickname("followingNickname")
                .profileImageUrl("followingProfileImageUrl1")
                .build();

        given(userRepository.findAllFollowings(anyLong())).willReturn(Arrays.asList(followingUser));

        // When
        FollowingServiceResponse followingServiceResponse = userService.getFollowings(FOLLOWER_ID);

        // Then
        assertAll(
                () -> assertThat(followingServiceResponse.getUserId()).isEqualTo(FOLLOWER_ID),
                () -> assertThat(followingServiceResponse.getFollowings()).hasSize(1),
                () -> assertThat(followingServiceResponse.getFollowings()).extracting("userId").containsExactly(FOLLOWING_ID),
                () -> assertThat(followingServiceResponse.getFollowings()).extracting("nickname").containsExactly(followingUser.getNickname()),
                () -> assertThat(followingServiceResponse.getFollowings()).extracting("profileImageUrl").containsExactly(followingUser.getProfileImageUrl())
        );

        verify(userRepository).findByLoginId(FOLLOWER_ID);
        verify(userRepository).findAllFollowings(anyLong());
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

    @Test
    @DisplayName("닉네임으로 사용자를 검색하면 해당 닉네임을 포함하는 사용자 목록을 조회한다")
    void searchUsersByNickname() {
        // Given
        given(userRepository.findByNicknameContaining("nickname")).willReturn(
                Arrays.asList(UserFixture.COMMON_USER.getUser(), UserFixture.USER_WITH_SIMILAR_NICKNAME.getUser()));
        given(storageService.getPathFromUrl("profileImageThumbnailTinyUrl")).willReturn("tiny-profileImagePath");
        given(storageService.getPathFromUrl("profileImageUrl")).willReturn("profileImagePath");


        // When
        SearchServiceResponse searchServiceResponse = userService.searchByNickname("nickname");

        // Then
        assertAll(
                () -> assertThat(searchServiceResponse.getSearchedUsers()).hasSize(2),
                () -> assertThat(searchServiceResponse.getSearchedUsers()).extracting("nickname").containsExactly("nickname", "nickname123")
        );

        verify(userRepository).findByNicknameContaining("nickname");
        verify(storageService, times(2)).getPathFromUrl("profileImageThumbnailTinyUrl");
        verify(storageService, times(2)).getPathFromUrl("profileImageUrl");
    }

    @Test
    @DisplayName("닉네임으로 사용자를 검색할 때 검색된 사용자가 없으면 빈 목록을 반환한다")
    void searchUsersByNicknameWithEmptyResult() {
        // Given
        given(userRepository.findByNicknameContaining("nickname")).willReturn(Collections.emptyList());

        // When
        SearchServiceResponse searchServiceResponse = userService.searchByNickname("nickname");

        // Then
        assertThat(searchServiceResponse.getSearchedUsers()).isEmpty();

        verify(userRepository).findByNicknameContaining("nickname");
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 계정을 삭제한다")
    void deleteAccount() {
        // Given
        User user = UserFixture.COMMON_USER.getUser();
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.of(user));

        // When
        userService.deleteAccount(LOGIN_ID);

        // Then
        verify(userRepository).findByLoginId(LOGIN_ID);
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자 계정을 삭제할 때 사용자가 없으면 예외를 던진다")
    void deleteAccountWithNotExistUser() {
        // Given
        given(userRepository.findByLoginId(LOGIN_ID)).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> userService.deleteAccount(LOGIN_ID))
                .isInstanceOf(UserNotFoundException.class)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue("errorCode", "U-05")
                .hasFieldOrPropertyWithValue("errorMessage", "존재하지 않는 사용자입니다.");

        // Then
        verify(userRepository).findByLoginId(LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 아이디 목록으로 사용자 정보를 조회한다")
    void getUserInfo() {
        // Given
        given(userRepository.findAllUserInfoByLoginIds(Arrays.asList("userId1", "userId2", "userId3"))).willReturn(
                Arrays.asList(UserInfoModel.builder().loginId("userId1").nickname("nickname1").profileImageUrl("profileImageUrl1").build(),
                        UserInfoModel.builder().loginId("userId2").nickname("nickname2").profileImageUrl("profileImageUrl2").build(),
                        UserInfoModel.builder().loginId("userId3").nickname("nickname3").profileImageUrl("profileImageUrl3").build()));

        given(userRepository.findByLoginId("userId1")).willReturn(Optional.ofNullable(User.builder()
                .loginId("userId1")
                .nickname("nickname1")
                .profileImageUrl("profileImageUrl1")
                .profileImageThumbnailTinyUrl("profileImageThumbnailTinyUrl1")
                .build()));
        given(storageService.getPathFromUrl("profileImageThumbnailTinyUrl1")).willReturn("tiny-profileImagePath1");
        given(storageService.getPathFromUrl("profileImageUrl1")).willReturn("profileImagePath1");

        given(userRepository.findByLoginId("userId2")).willReturn(Optional.ofNullable(User.builder()
                .loginId("userId2")
                .nickname("nickname2")
                .profileImageUrl("profileImageUrl2")
                .profileImageThumbnailTinyUrl("profileImageThumbnailTinyUrl2")
                .build()));
        given(storageService.getPathFromUrl("profileImageThumbnailTinyUrl2")).willReturn("tiny-profileImagePath2");
        given(storageService.getPathFromUrl("profileImageUrl2")).willReturn("profileImagePath2");

        given(userRepository.findByLoginId("userId3")).willReturn(Optional.ofNullable(User.builder()
                .loginId("userId3")
                .nickname("nickname3")
                .profileImageUrl("profileImageUrl3")
                .profileImageThumbnailTinyUrl("profileImageThumbnailTinyUrl3")
                .build()));
        given(storageService.getPathFromUrl("profileImageThumbnailTinyUrl3")).willReturn("tiny-profileImagePath3");
        given(storageService.getPathFromUrl("profileImageUrl3")).willReturn("profileImagePath3");


        // When
        UserInfoServiceResponse userInfoServiceResponse = userService.getUserInfo(
                UserInfoServiceRequest.builder().userIds(Arrays.asList("userId1", "userId2", "userId3")).build());

        // Then
        assertAll(
                () -> assertThat(userInfoServiceResponse.getUserInfoElements()).hasSize(3),
                () -> assertThat(userInfoServiceResponse.getUserInfoElements()).extracting("userId").containsExactly("userId1", "userId2", "userId3"),
                () -> assertThat(userInfoServiceResponse.getUserInfoElements()).extracting("nickname").containsExactly("nickname1", "nickname2", "nickname3"),
                () -> assertThat(userInfoServiceResponse.getUserInfoElements()).extracting("profileImageUrl").containsExactly("profileImageThumbnailTinyUrl1", "profileImageThumbnailTinyUrl2", "profileImageThumbnailTinyUrl3")
        );

        verify(userRepository).findAllUserInfoByLoginIds(Arrays.asList("userId1", "userId2", "userId3"));
        verify(userRepository, times(3)).findByLoginId(anyString());
        verify(storageService, times(6)).getPathFromUrl(anyString());
    }

}