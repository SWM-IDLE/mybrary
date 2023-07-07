package kr.mybrary.userservice.user.domain;

import jakarta.transaction.Transactional;
import kr.mybrary.userservice.user.domain.dto.UserMapper;
import kr.mybrary.userservice.user.domain.dto.request.FollowServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.FollowerServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.ProfileImageServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.ProfileUpdateServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.userservice.user.domain.dto.response.FollowerServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.FollowingServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.ProfileImageServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.ProfileServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.SignUpServiceResponse;
import kr.mybrary.userservice.user.domain.exception.DuplicateEmailException;
import kr.mybrary.userservice.user.domain.exception.DuplicateLoginIdException;
import kr.mybrary.userservice.user.domain.exception.DuplicateNicknameException;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        SignUpServiceResponse serviceResponse = UserMapper.INSTANCE.toSignUpServiceResponse(userRepository.save(user));

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
        return null;
    }

    @Override
    public ProfileServiceResponse updateProfile(ProfileUpdateServiceRequest serviceRequest) {
        return null;
    }

    @Override
    public ProfileImageServiceResponse getProfileImage(String loginId) {
        return null;
    }

    @Override
    public ProfileImageServiceResponse updateProfileImage(
            ProfileImageServiceRequest serviceRequest) {
        return null;
    }

    @Override
    public ProfileImageServiceResponse deleteProfileImage(String loginId) {
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
