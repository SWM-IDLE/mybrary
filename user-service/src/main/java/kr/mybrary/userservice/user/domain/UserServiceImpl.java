package kr.mybrary.userservice.user.domain;

import jakarta.transaction.Transactional;
import kr.mybrary.userservice.user.domain.dto.UserMapper;
import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.userservice.user.domain.dto.response.ProfileServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.SignUpServiceResponse;
import kr.mybrary.userservice.user.domain.exception.DuplicateEmailException;
import kr.mybrary.userservice.user.domain.exception.DuplicateLoginIdException;
import kr.mybrary.userservice.user.domain.exception.DuplicateNicknameException;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Override
    public User grantUserRole(String loginId) {
        User findUser = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException(loginId));
        findUser.updateRole(Role.USER);
        return userRepository.save(findUser);
    }

    @Override
    public ProfileServiceResponse getProfile(String loginId) {
        return null;
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

}
