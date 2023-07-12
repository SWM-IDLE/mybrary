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

        validateDuplicateLoginId(serviceRequest.getLoginId());
        validateDuplicateNickname(serviceRequest.getNickname());
        validateDuplicateEmail(serviceRequest.getEmail());

        User user = UserMapper.INSTANCE.toEntity(serviceRequest);
        user.updatePassword(passwordEncoder.encode(user.getPassword()));
        user.updateRole(Role.USER);
        SignUpServiceResponse serviceResponse = UserMapper.INSTANCE.toSignUpServiceResponse(
                userRepository.save(user));

        return serviceResponse;
    }

    private void validateDuplicateEmail(String email) {
        // TODO: existsByEmail() 메서드를 사용하면 더 간결하게 구현할 수 있을 것 같다.
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException();
        }
    }

    private void validateDuplicateNickname(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new DuplicateNicknameException();
        }
    }

    private void validateDuplicateLoginId(String loginId) {
        if (userRepository.findByLoginId(loginId).isPresent()) {
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
        validateDuplicateNickname(serviceRequest.getNickname());
        // TODO: 이메일 중복 검사는 필요한가? 프로필에 등록되는 이메일과 회원 가입 시 사용되는 이메일이 다를 수 있는가?
        validateDuplicateEmail(serviceRequest.getEmail());

        User user = userRepository.findByLoginId(serviceRequest.getLoginId())
                .orElseThrow(UserNotFoundException::new);
        user.updateProfile(serviceRequest.getNickname(), serviceRequest.getEmail(), serviceRequest.getIntroduction());
        ProfileServiceResponse serviceResponse = UserMapper.INSTANCE.toProfileServiceResponse(user);

        return serviceResponse;
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
    public ProfileImageUrlServiceResponse updateProfileImage(
            ProfileImageUpdateServiceRequest serviceRequest) {
        return null;
    }

    @Override
    public ProfileImageUrlServiceResponse deleteProfileImage(String loginId) {
        return null;
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
