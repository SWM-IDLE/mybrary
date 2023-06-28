package kr.mybrary.userservice.authentication.domain.impl;

import jakarta.transaction.Transactional;
import kr.mybrary.userservice.authentication.domain.AuthenticationService;
import kr.mybrary.userservice.authentication.domain.dto.UserMapper;
import kr.mybrary.userservice.authentication.domain.exception.DuplicateEmailException;
import kr.mybrary.userservice.authentication.domain.exception.DuplicateLoginIdException;
import kr.mybrary.userservice.authentication.domain.exception.DuplicateNicknameException;
import kr.mybrary.userservice.authentication.presentation.dto.request.SignUpRequest;
import kr.mybrary.userservice.authentication.presentation.dto.response.SignUpResponse;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) {

        validateDuplicateLoginId(signUpRequest);
        validateDuplicateNickname(signUpRequest);
        validateDuplicateEmail(signUpRequest);

        User user = UserMapper.INSTANCE.toEntity(signUpRequest);
        user.updatePassword(passwordEncoder.encode(user.getPassword()));
        user.updateRole(Role.USER);
        SignUpResponse signUpResponse = UserMapper.INSTANCE.toResponse(userRepository.save(user));

        return signUpResponse;

    }

    @Override
    public UserDetails loadUserByUsername(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException(loginId));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLoginId())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
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
