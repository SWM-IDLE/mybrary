package kr.mybrary.userservice.user.domain;

import kr.mybrary.userservice.global.util.MultipartFileUtil;
import kr.mybrary.userservice.user.domain.dto.UserMapper;
import kr.mybrary.userservice.user.domain.dto.request.FollowServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.FollowerServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.ProfileImageUpdateServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.ProfileUpdateServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.userservice.user.domain.dto.response.*;
import kr.mybrary.userservice.user.domain.exception.follow.DuplicateFollowException;
import kr.mybrary.userservice.user.domain.exception.follow.SameSourceTargetUserException;
import kr.mybrary.userservice.user.domain.exception.user.DuplicateLoginIdException;
import kr.mybrary.userservice.user.domain.exception.user.DuplicateNicknameException;
import kr.mybrary.userservice.user.domain.exception.io.EmptyFileException;
import kr.mybrary.userservice.user.domain.exception.profile.ProfileImageFileSizeExceededException;
import kr.mybrary.userservice.user.domain.exception.profile.ProfileImageUrlNotFoundException;
import kr.mybrary.userservice.user.domain.exception.user.UserNotFoundException;
import kr.mybrary.userservice.user.domain.exception.user.UserNotSearchedException;
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

import java.util.List;
import java.util.stream.Collectors;

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
    public UserResponse getUserResponse(String loginId) {
        return UserResponse.builder().user(getUser(loginId)).build();
    }

    private User getUser(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileServiceResponse getProfile(String loginId) {
        User user = getUser(loginId);
        ProfileServiceResponse serviceResponse = UserMapper.INSTANCE.toProfileServiceResponse(user);

        return serviceResponse;
    }

    @Override
    public ProfileServiceResponse updateProfile(ProfileUpdateServiceRequest serviceRequest) {
        User user = getUser(serviceRequest.getLoginId());

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
        User user = getUser(loginId);

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

        User user = getUser(serviceRequest.getLoginId());

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
        User user = getUser(loginId);

        user.updateProfileImageUrl(DEFAULT_PROFILE_IMAGE_URL);
        ProfileImageUrlServiceResponse serviceResponse = ProfileImageUrlServiceResponse.builder()
                .profileImageUrl(user.getProfileImageUrl())
                .build();

        return serviceResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public FollowerServiceResponse getFollowers(String loginId) {
        User user = getUser(loginId);

        FollowerServiceResponse serviceResponse = FollowerServiceResponse.builder()
                .requestLoginId(loginId)
                .followers(getFollowerResponses(user))
                .build();

        return serviceResponse;
    }

    private List<FollowResponse> getFollowerResponses(User user) {
        return user.getFollowers().stream()
                .map(follow -> FollowResponse.builder()
                        .id(follow.getId())
                        .loginId(follow.getSource().getLoginId())
                        .nickname(follow.getSource().getNickname())
                        .profileImageUrl(follow.getSource().getProfileImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FollowingServiceResponse getFollowings(String loginId) {
        User user = getUser(loginId);

        FollowingServiceResponse serviceResponse = FollowingServiceResponse.builder()
                .requestLoginId(loginId)
                .followings(getFollowingResponses(user))
                .build();

        return serviceResponse;
    }

    private List<FollowResponse> getFollowingResponses(User user) {
        return user.getFollowings().stream()
                .map(follow -> FollowResponse.builder()
                        .id(follow.getId())
                        .loginId(follow.getTarget().getLoginId())
                        .nickname(follow.getTarget().getNickname())
                        .profileImageUrl(follow.getTarget().getProfileImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void follow(FollowServiceRequest serviceRequest) {
        User sourceUser = getUser(serviceRequest.getSourceId());
        User targetUser = getUser(serviceRequest.getTargetId());

        validateDifferentSourceTarget(sourceUser, targetUser);
        validateDuplicateFollow(sourceUser, targetUser);

        sourceUser.follow(targetUser);
    }

    private void validateDifferentSourceTarget(User source, User target) {
        if (source.equals(target)) {
            throw new SameSourceTargetUserException();
        }
    }

    private void validateDuplicateFollow(User source, User target) {
        if (source.getFollowings().stream()
                .anyMatch(follow -> follow.getTarget().equals(target))) {
            throw new DuplicateFollowException();
        }
    }

    @Override
    public void unfollow(FollowServiceRequest serviceRequest) {
        User sourceUser = getUser(serviceRequest.getSourceId());
        User targetUser = getUser(serviceRequest.getTargetId());

        validateDifferentSourceTarget(sourceUser, targetUser);

        sourceUser.unfollow(targetUser);
    }

    // TODO: deleteFollower 로직 논의 -> 해당 유저가 본인을 다시 팔로우할 수 있는가?
    @Override
    public void deleteFollower(FollowerServiceRequest serviceRequest) {
        User sourceUser = getUser(serviceRequest.getSourceId());
        User targetUser = getUser(serviceRequest.getTargetId());

        validateDifferentSourceTarget(sourceUser, targetUser);

        sourceUser.unfollow(targetUser);
    }

    @Override
    public SearchServiceResponse searchByNickname(String nickname) {
        List<SearchServiceResponse.SearchedUser> searchedUsers = userRepository.findByNicknameContaining(nickname).stream()
                .map(user -> UserMapper.INSTANCE.toSearchedUser(user))
                .collect(Collectors.toList());

        checkIfUserNotSearched(searchedUsers);

        SearchServiceResponse serviceResponse = SearchServiceResponse.builder()
                .searchedUsers(searchedUsers)
                .build();

        return serviceResponse;
    }

    private static void checkIfUserNotSearched(List<SearchServiceResponse.SearchedUser> searchedUsers) {
        if(searchedUsers.isEmpty()) {
            throw new UserNotSearchedException();
        }
    }


}