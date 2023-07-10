package kr.mybrary.userservice.user.domain;

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
import kr.mybrary.userservice.user.domain.exception.DuplicateEmailException;
import kr.mybrary.userservice.user.domain.exception.DuplicateLoginIdException;
import kr.mybrary.userservice.user.domain.exception.DuplicateNicknameException;
import kr.mybrary.userservice.user.domain.exception.ProfileImageUrlNotFoundException;
import kr.mybrary.userservice.user.domain.exception.UserNotFoundException;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SignUpServiceResponse signUp(SignUpServiceRequest serviceRequest) {

        validateDuplicateLoginId(serviceRequest);
        validateDuplicateNickname(serviceRequest);
        validateDuplicateEmail(serviceRequest);

        User user = UserMapper.INSTANCE.toEntity(serviceRequest);
        user.updatePassword(passwordEncoder.encode(user.getPassword()));
        user.updateRole(Role.USER);
        SignUpServiceResponse serviceResponse = UserMapper.INSTANCE.toSignUpServiceResponse(
                userRepository.save(user));

        return serviceResponse;
    }

    private void validateDuplicateEmail(SignUpServiceRequest serviceRequest) {
        if (userRepository.findByEmail(serviceRequest.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }
    }

    private void validateDuplicateNickname(SignUpServiceRequest serviceRequest) {
        if (userRepository.findByNickname(serviceRequest.getNickname()).isPresent()) {
            throw new DuplicateNicknameException();
        }
    }

    private void validateDuplicateLoginId(SignUpServiceRequest serviceRequest) {
        if (userRepository.findByLoginId(serviceRequest.getLoginId()).isPresent()) {
            throw new DuplicateLoginIdException();
        }
    }

    @Override
    public ProfileServiceResponse getProfile(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(UserNotFoundException::new);
        ProfileServiceResponse serviceResponse = UserMapper.INSTANCE.toProfileServiceResponse(user);

        return serviceResponse;
    }

    @Override
    public ProfileServiceResponse updateProfile(ProfileUpdateServiceRequest serviceRequest) {
        return null;
    }

    @Override
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
    public ProfileImageUrlServiceResponse updateProfileImage(
            ProfileImageUpdateServiceRequest serviceRequest) {
        return null;
    }

    @Override
    public ProfileImageUrlServiceResponse deleteProfileImage(String loginId) {
        return null;
    }

    @Override
    public FollowerServiceResponse getFollowers(String loginId) {
        return null;
    }

    @Override
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
