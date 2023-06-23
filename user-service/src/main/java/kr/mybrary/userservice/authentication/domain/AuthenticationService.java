package kr.mybrary.userservice.authentication.domain;

import jakarta.transaction.Transactional;
import kr.mybrary.userservice.authentication.domain.dto.UserMapper;
import kr.mybrary.userservice.authentication.domain.exception.DuplicateUserInfoException;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.authentication.presentation.dto.request.SignUpRequest;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User signUp(SignUpRequest signUpRequest) {

        if (userRepository.findByLoginId(signUpRequest.getLoginId()).isPresent()) {
            throw new DuplicateUserInfoException();
        }
        if (userRepository.findByNickname(signUpRequest.getNickname()).isPresent()) {
            throw new DuplicateUserInfoException();
        }
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new DuplicateUserInfoException();
        }

        User user = UserMapper.INSTANCE.toEntity(signUpRequest);
        user.updatePassword(passwordEncoder.encode(user.getPassword()));
        user.updateRole(Role.USER);

        return userRepository.save(user);

    }


}
