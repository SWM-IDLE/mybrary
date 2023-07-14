package kr.mybrary.userservice.user.domain;

import kr.mybrary.userservice.global.util.MultipartFileUtil;
import kr.mybrary.userservice.user.domain.dto.UserMapper;
import kr.mybrary.userservice.user.domain.dto.request.FollowServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.FollowerServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.ProfileImageUpdateServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.ProfileUpdateServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.userservice.user.domain.dto.response.FollowerServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.FollowingServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.ProfileImageUrlServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.ProfileServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.SignUpServiceResponse;
import kr.mybrary.userservice.user.domain.exception.user.DuplicateLoginIdException;
import kr.mybrary.userservice.user.domain.exception.user.DuplicateNicknameException;
import kr.mybrary.userservice.user.domain.exception.file.EmptyFileException;
import kr.mybrary.userservice.user.domain.exception.profile.ProfileImageFileSizeExceededException;
import kr.mybrary.userservice.user.domain.exception.profile.ProfileImageUrlNotFoundException;
import kr.mybrary.userservice.user.domain.exception.user.UserNotFoundException;
import kr.mybrary.userservice.user.domain.storage.StorageService;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // TODO: StorageService 결합도 낮추기
    private final StorageService storageService;

    private static final String DEFAULT_PROFILE_IMAGE_URL = "https://mybrary-user-service.s3.ap-northeast-2.amazonaws.com/profile/profileImage/default.jpg";
    private static final String PROFILE_IMAGE_PATH = "profile/profileImage/";
    private static final int MAX_PROFILE_IMAGE_SIZE = 5 * 1024 * 1024;

    @Override
    public SignUpServiceResponse signUp(SignUpServiceRequest serviceRequest) {

        validateDuplicateLoginId(serviceRequest.getLoginId());
        validateDuplicateNickname(serviceRequest.getNickname());

        User user = UserMapper.INSTANCE.toEntity(serviceRequest);
        user.updatePassword(passwordEncoder.encode(user.getPassword()));
        user.updateRole(Role.USER);
        user.updateProfileImageUrl(DEFAULT_PROFILE_IMAGE_URL);
        SignUpServiceResponse serviceResponse = UserMapper.INSTANCE.toSignUpServiceResponse(
                userRepository.save(user));

        return serviceResponse;
    }

    private void validateDuplicateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException();
        }
    }

    private void validateDuplicateLoginId(String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            throw new DuplicateLoginIdException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileServiceResponse getProfile(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(UserNotFoundException::new);
        ProfileServiceResponse serviceResponse = UserMapper.INSTANCE.toProfileServiceResponse(user);

        return serviceResponse;
    }

    @Override
    public ProfileServiceResponse updateProfile(ProfileUpdateServiceRequest serviceRequest) {
        User user = userRepository.findByLoginId(serviceRequest.getLoginId())
                .orElseThrow(UserNotFoundException::new);

        checkIfNicknameUpdateIsPossible(user.getNickname(), serviceRequest.getNickname());

        user.updateProfile(serviceRequest.getNickname(), serviceRequest.getIntroduction());
        ProfileServiceResponse serviceResponse = UserMapper.INSTANCE.toProfileServiceResponse(user);

        return serviceResponse;
    }

    private void checkIfNicknameUpdateIsPossible(String userNickname, String requestNickname) {
        if (!userNickname.equals(requestNickname)) {
            validateDuplicateNickname(requestNickname);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileImageUrlServiceResponse getProfileImageUrl(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(UserNotFoundException::new);

        checkProfileImageUrlExistence(user);

        ProfileImageUrlServiceResponse serviceResponse = ProfileImageUrlServiceResponse.builder()
                .profileImageUrl(user.getProfileImageUrl())
                .build();

        return serviceResponse;
    }

    private void checkProfileImageUrlExistence(User user) {
        if (user.getProfileImageUrl() == null) {
            throw new ProfileImageUrlNotFoundException();
        }
    }

    @Override
    public ProfileImageUrlServiceResponse updateProfileImage(ProfileImageUpdateServiceRequest serviceRequest) {
        checkProfileImageExistence(serviceRequest.getProfileImage());
        checkProfileImageSize(serviceRequest.getProfileImage());

        User user = userRepository.findByLoginId(serviceRequest.getLoginId())
                .orElseThrow(UserNotFoundException::new);

        String profileImageUrl = storageService.putFile(serviceRequest.getProfileImage(),
                MultipartFileUtil.generateFilePath(PROFILE_IMAGE_PATH, serviceRequest.getLoginId(), serviceRequest.getProfileImage()));

        user.updateProfileImageUrl(profileImageUrl);
        ProfileImageUrlServiceResponse serviceResponse = ProfileImageUrlServiceResponse.builder()
                .profileImageUrl(user.getProfileImageUrl())
                .build();

        return serviceResponse;
    }

    private void checkProfileImageExistence(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new EmptyFileException();
        }
    }

    private void checkProfileImageSize(MultipartFile multipartFile) {
        if (multipartFile.getSize() > MAX_PROFILE_IMAGE_SIZE) {
            throw new ProfileImageFileSizeExceededException();
        }
    }

    @Override
    public ProfileImageUrlServiceResponse deleteProfileImage(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(UserNotFoundException::new);

        user.updateProfileImageUrl(DEFAULT_PROFILE_IMAGE_URL);
        ProfileImageUrlServiceResponse serviceResponse = ProfileImageUrlServiceResponse.builder()
                .profileImageUrl(user.getProfileImageUrl())
                .build();

        return serviceResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public FollowerServiceResponse getFollowers(String loginId) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public FollowingServiceResponse getFollowings(String loginId) {
        return null;
    }

    @Override
    public void follow(FollowServiceRequest serviceRequest) {
    }

    @Override
    public void unfollow(FollowServiceRequest serviceRequest) {

    }

    @Override
    public void deleteFollower(FollowerServiceRequest serviceRequest) {

    }


}
