package kr.mybrary.userservice.user.domain;

import jakarta.validation.constraints.NotNull;
import kr.mybrary.userservice.global.util.MultipartFileUtil;
import kr.mybrary.userservice.user.domain.dto.UserMapper;
import kr.mybrary.userservice.user.domain.dto.request.*;
import kr.mybrary.userservice.user.domain.dto.response.*;
import kr.mybrary.userservice.user.domain.exception.follow.DuplicateFollowException;
import kr.mybrary.userservice.user.domain.exception.follow.SameSourceTargetUserException;
import kr.mybrary.userservice.user.domain.exception.profile.ProfileImageSizeOptionNotSupportedException;
import kr.mybrary.userservice.user.domain.exception.profile.ProfileUpdateRequestNotAuthenticated;
import kr.mybrary.userservice.user.domain.exception.user.DuplicateLoginIdException;
import kr.mybrary.userservice.user.domain.exception.user.DuplicateNicknameException;
import kr.mybrary.userservice.user.domain.exception.io.EmptyFileException;
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

import java.util.List;
import java.util.stream.Collectors;

import static kr.mybrary.userservice.global.constant.ImageConstant.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;
    private static final String PROFILE_IMAGE_PATH_FORMAT = "profile/profileImage/%s/";
    private static final int MAX_PROFILE_IMAGE_SIZE = 5 * 1024 * 1024;
    private static final String PROFILE_IMAGE_SIZE_TINY = "tiny";
    private static final String PROFILE_IMAGE_SIZE_SMALL = "small";

    @Override
    public SignUpServiceResponse signUp(SignUpServiceRequest serviceRequest) {

        validateDuplicateLoginId(serviceRequest.getLoginId());
        validateDuplicateNickname(serviceRequest.getNickname());

        User user = UserMapper.INSTANCE.toEntity(serviceRequest);
        user.updatePassword(passwordEncoder.encode(user.getPassword()));
        user.updateRole(Role.USER);
        setDefaultProfileImage(user);
        SignUpServiceResponse serviceResponse = UserMapper.INSTANCE.toSignUpServiceResponse(
                userRepository.save(user));

        return serviceResponse;
    }

    private void setDefaultProfileImage(User user) {
        user.updateProfileImageUrl(DEFAULT_PROFILE_IMAGE.getUrl());
        user.updateProfileImageThumbnailTinyUrl(DEFAULT_PROFILE_IMAGE_TINY.getUrl());
        user.updateProfileImageThumbnailSmallUrl(DEFAULT_PROFILE_IMAGE_SMALL.getUrl());
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
        checkProfileUpdateRequestAuthentication(serviceRequest);

        User user = getUser(serviceRequest.getLoginId());

        checkIfNicknameUpdateIsPossible(user.getNickname(), serviceRequest.getNickname());

        user.updateProfile(serviceRequest.getNickname(), serviceRequest.getIntroduction());
        ProfileServiceResponse serviceResponse = UserMapper.INSTANCE.toProfileServiceResponse(user);

        return serviceResponse;
    }

    private void checkProfileUpdateRequestAuthentication(ProfileUpdateServiceRequest serviceRequest) {
        if(!serviceRequest.getUserId().equals(serviceRequest.getLoginId())) {
            throw new ProfileUpdateRequestNotAuthenticated();
        }
    }

    private void checkIfNicknameUpdateIsPossible(String userNickname, String requestNickname) {
        if (!userNickname.equals(requestNickname)) {
            validateDuplicateNickname(requestNickname);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileImageUrlServiceResponse getProfileImageUrl(ProfileImageUrlServiceRequest serviceRequest) {
        return ProfileImageUrlServiceResponse.builder()
                .profileImageUrl(getProfileImageUrlBy(getUser(serviceRequest.getUserId()), serviceRequest.getSize()))
                .build();
    }

    private String getProfileImageUrlBy(User user, String size) {
        if(size.equals("original")) {
            return getOriginalProfileImageUrl(user);
        }
        return getResizedProfileImageUrl(user, size);
    }

    private String getOriginalProfileImageUrl(User user) {
        checkProfileImageUrlExistence(user);
        return user.getProfileImageUrl();
    }

    private void checkProfileImageUrlExistence(User user) {
        if (user.getProfileImageUrl() == null) {
            throw new ProfileImageUrlNotFoundException();
        }
    }

    private String getResizedProfileImageUrl(User user, String size) {
        if(isProfileImageThumbnailUrlUpdated(user, size)) {
            return getProfileImageThumbnailUrl(user, size);
        }
        if(storageService.hasResizedFiles(storageService.getPathFromUrl(user.getProfileImageUrl()), size)) {
            updateProfileImageThumbnailUrl(user, size);
            return getProfileImageThumbnailUrl(user, size);
        }
        return getOriginalProfileImageUrl(user);
    }

    private boolean isProfileImageThumbnailUrlUpdated(User user, String size) {
        return storageService.getPathFromUrl(getProfileImageThumbnailUrl(user, size))
                .equals(size + "-" + storageService.getPathFromUrl(user.getProfileImageUrl()));
    }

    private String getProfileImageThumbnailUrl(User user, String size) {
        if(size.equals(PROFILE_IMAGE_SIZE_TINY)) {
            return user.getProfileImageThumbnailTinyUrl();
        }
        if(size.equals(PROFILE_IMAGE_SIZE_SMALL)) {
            return user.getProfileImageThumbnailSmallUrl();
        }
        throw new ProfileImageSizeOptionNotSupportedException();
    }

    private void updateProfileImageThumbnailUrl(User user, String size) {
        if(size.equals(PROFILE_IMAGE_SIZE_TINY)) {
            user.updateProfileImageThumbnailTinyUrl(storageService.getResizedUrl(user.getProfileImageUrl(), size));
            return;
        }
        if(size.equals(PROFILE_IMAGE_SIZE_SMALL)) {
            user.updateProfileImageThumbnailSmallUrl(storageService.getResizedUrl(user.getProfileImageUrl(), size));
        }
    }

    @Override
    public ProfileImageUrlServiceResponse updateProfileImage(ProfileImageUpdateServiceRequest serviceRequest) {
        checkProfileImageUpdateRequestAuthentication(serviceRequest);
        checkProfileImageExistence(serviceRequest.getProfileImage());
        checkProfileImageSize(serviceRequest.getProfileImage());

        User user = getUser(serviceRequest.getLoginId());

        String profileImageUrl = storageService.putFile(serviceRequest.getProfileImage(),
                MultipartFileUtil.generateFilePath(String.format(PROFILE_IMAGE_PATH_FORMAT, serviceRequest.getLoginId()),
                        MultipartFileUtil.generateTimestampFileName(serviceRequest.getLocalDateTime()),
                        MultipartFileUtil.getFileExtension(serviceRequest.getProfileImage())));

        user.updateProfileImageUrl(profileImageUrl);
        ProfileImageUrlServiceResponse serviceResponse = ProfileImageUrlServiceResponse.builder()
                .profileImageUrl(user.getProfileImageUrl())
                .build();

        return serviceResponse;
    }

    private void checkProfileImageUpdateRequestAuthentication(ProfileImageUpdateServiceRequest serviceRequest) {
        if(!serviceRequest.getUserId().equals(serviceRequest.getLoginId())) {
            throw new ProfileUpdateRequestNotAuthenticated();
        }
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
    public ProfileImageUrlServiceResponse deleteProfileImage(ProfileImageUpdateServiceRequest serviceRequest) {
        checkProfileImageUpdateRequestAuthentication(serviceRequest);

        User user = getUser(serviceRequest.getLoginId());
        setDefaultProfileImage(user);
        ProfileImageUrlServiceResponse serviceResponse = ProfileImageUrlServiceResponse.builder()
                .profileImageUrl(user.getProfileImageUrl())
                .build();

        return serviceResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public FollowerServiceResponse getFollowers(String loginId) {
        return FollowerServiceResponse.builder()
                .userId(loginId)
                .followers(getFollowerResponses(getUser(loginId)))
                .build();
    }

    private List<FollowResponse> getFollowerResponses(User user) {
        return userRepository.findAllFollowers(user.getId()).stream()
                .map(followUserInfoModel -> FollowResponse.of(followUserInfoModel))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FollowingServiceResponse getFollowings(String loginId) {
        return FollowingServiceResponse.builder()
                .userId(loginId)
                .followings(getFollowingResponses(getUser(loginId)))
                .build();
    }

    private List<FollowResponse> getFollowingResponses(User user) {
        return userRepository.findAllFollowings(user.getId()).stream()
                .map(followUserInfoModel -> FollowResponse.of(followUserInfoModel))
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
        if (checkFollowing(source, target)) {
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
    @Transactional(readOnly = true)
    public FollowStatusServiceResponse getFollowStatus(FollowServiceRequest serviceRequest) {
        return FollowStatusServiceResponse.builder()
                .userId(serviceRequest.getSourceId())
                .targetId(serviceRequest.getTargetId())
                .isFollowing(checkFollowing(getUser(serviceRequest.getSourceId()), getUser(serviceRequest.getTargetId())))
                .build();
    }

    private boolean checkFollowing(User sourceUser, User targetUser) {
        return sourceUser.getFollowings().stream().anyMatch(follow -> follow.getTarget().equals(targetUser));
    }

    @Override
    public SearchServiceResponse searchByNickname(String nickname) {
        List<SearchServiceResponse.SearchedUser> searchedUsers = userRepository.findByNicknameContaining(nickname).stream()
                .map(user -> SearchServiceResponse.SearchedUser.builder()
                        .userId(user.getLoginId())
                        .nickname(user.getNickname())
                        .profileImageUrl(getResizedProfileImageUrl(user, PROFILE_IMAGE_SIZE_TINY))
                        .build())
                .collect(Collectors.toList());

        SearchServiceResponse serviceResponse = SearchServiceResponse.builder()
                .searchedUsers(searchedUsers)
                .build();

        return serviceResponse;
    }

    @Override
    public void deleteAccount(String loginId) {
        User user = getUser(loginId);
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoServiceResponse getUserInfo(UserInfoServiceRequest serviceRequest) {
        return UserInfoServiceResponse.builder()
                .userInfoElements(getUserInfoElements(serviceRequest))
                .build();
    }

    // TODO: 논의 필요 - getUser 메서드에서 쿼리가 한 번 더 나가는 문제
    @NotNull
    private List<UserInfoServiceResponse.UserInfoElement> getUserInfoElements(UserInfoServiceRequest serviceRequest) {
        return userRepository.findAllUserInfoByLoginIds(serviceRequest.getUserIds()).stream()
                .map(userInfoModel -> UserInfoServiceResponse.UserInfoElement.builder()
                        .userId(userInfoModel.getLoginId())
                        .nickname(userInfoModel.getNickname())
                        .profileImageUrl(getResizedProfileImageUrl(getUser(userInfoModel.getLoginId()), PROFILE_IMAGE_SIZE_TINY))
                        .build())
                .collect(Collectors.toList());
    }

}
