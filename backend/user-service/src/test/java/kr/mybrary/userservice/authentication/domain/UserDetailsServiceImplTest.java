package kr.mybrary.userservice.authentication.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import kr.mybrary.userservice.authentication.UserDetailsServiceImpl;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserDetailsServiceImpl authenticationService;

    @Test
    @DisplayName("로그인 아이디를 통해 사용자 정보를 조회한다")
    void loadUserByUsername() {
        // Given
        String loginId = "loginId";
        User user = User.builder()
                .loginId(loginId)
                .password("password")
                .nickname("nickname")
                .role(Role.USER)
                .build();

        given(userRepository.findByLoginId(loginId)).willReturn(Optional.of(user));

        // When
        UserDetails userDetails = authenticationService.loadUserByUsername(loginId);

        // Then
        assertThat(userDetails.getUsername()).isEqualTo(loginId);
        Assertions.assertAll(
                () -> assertThat(userDetails.getUsername()).isEqualTo(loginId),
                () -> assertThat(userDetails.getPassword()).isEqualTo(user.getPassword())
        );

        verify(userRepository).findByLoginId(loginId);
    }

    @Test
    @DisplayName("로그인 아이디로 사용자를 찾을 때 로그인 아이디와 일치하는 사용자가 없으면 예외를 던진다")
    void usernameNotFoundWhenLoadByUsername() {
        // Given
        String loginId = "loginId";

        given(userRepository.findByLoginId(loginId)).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> authenticationService.loadUserByUsername(loginId))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(loginId);

        // Then
        verify(userRepository).findByLoginId(loginId);
    }

}