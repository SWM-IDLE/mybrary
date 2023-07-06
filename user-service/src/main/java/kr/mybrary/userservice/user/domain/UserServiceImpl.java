package kr.mybrary.userservice.user.domain;

import jakarta.transaction.Transactional;
import kr.mybrary.userservice.user.domain.dto.UserMapper;
import kr.mybrary.userservice.user.domain.exception.DuplicateEmailException;
import kr.mybrary.userservice.user.domain.exception.DuplicateLoginIdException;
import kr.mybrary.userservice.user.domain.exception.DuplicateNicknameException;
import kr.mybrary.userservice.user.domain.dto.response.ProfileServiceResponse;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import kr.mybrary.userservice.user.presentation.dto.request.SignUpRequest;
import kr.mybrary.userservice.user.presentation.dto.response.SignUpResponse;
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
    public SignUpResponse signUp(SignUpRequest signUpRequest) {

        validateDuplicateLoginId(signUpRequest);
        validateDuplicateNickname(signUpRequest);
        validateDuplicateEmail(signUpRequest);

        User user = UserMapper.INSTANCE.toEntity(signUpRequest);
        user.updatePassword(passwordEncoder.encode(user.getPassword()));
        grantUserRole(user.getLoginId());
        SignUpResponse signUpResponse = UserMapper.INSTANCE.toResponse(userRepository.save(user));

        return signUpResponse;
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

    private void validateDuplicateEmail(SignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }
    }

    private void validateDuplicateNickname(SignUpRequest signUpRequest) {
        if (userRepository.findByNickname(signUpRequest.getNickname()).isPresent()) {
            throw new DuplicateNicknameException();
        }
    }

    private void validateDuplicateLoginId(SignUpRequest signUpRequest) {
        if (userRepository.findByLoginId(signUpRequest.getLoginId()).isPresent()) {
            throw new DuplicateLoginIdException();
        }
    }

}
