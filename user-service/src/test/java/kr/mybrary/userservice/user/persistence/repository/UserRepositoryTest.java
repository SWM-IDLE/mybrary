package kr.mybrary.userservice.user.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import kr.mybrary.userservice.user.UserTestData;
import kr.mybrary.userservice.user.persistence.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("로그인 아이디로 사용자를 가져온다.")
    void findByLoginId() {
        // given
        User savedUser = userRepository.save(UserTestData.createUser());

        // when
        Optional<User> foundUser = userRepository.findByLoginId(savedUser.getLoginId());

        // then
        assertAll(
            () -> assertThat(foundUser).isPresent(),
            () -> assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId()),
            () -> assertThat(foundUser.get().getLoginId()).isEqualTo(savedUser.getLoginId())
        );

    }

    @Test
    @DisplayName("이메일로 사용자를 가져온다.")
    void findByEmail() {
        // given
        User savedUser = userRepository.save(UserTestData.createUser());

        // when
        Optional<User> foundUser = userRepository.findByEmail(savedUser.getEmail());

        // then
        assertAll(
            () -> assertThat(foundUser).isPresent(),
            () -> assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId()),
            () -> assertThat(foundUser.get().getEmail()).isEqualTo(savedUser.getEmail())
        );
    }

    @Test
    @DisplayName("닉네임으로 사용자를 가져온다.")
    void findByNickname() {
        // given
        User savedUser = userRepository.save(UserTestData.createUser());

        // when
        Optional<User> foundUser = userRepository.findByNickname(savedUser.getNickname());

        // then
        assertAll(
            () -> assertThat(foundUser).isPresent(),
            () -> assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId()),
            () -> assertThat(foundUser.get().getNickname()).isEqualTo(savedUser.getNickname())
        );
    }

    @Test
    @DisplayName("리프레쉬 토큰으로 사용자를 가져온다.")
    void findByRefreshToken() {
        // given
        User savedUser = userRepository.save(UserTestData.createUser());

        // when
        Optional<User> foundUser = userRepository.findByRefreshToken(savedUser.getRefreshToken());

        // then
        assertAll(
            () -> assertThat(foundUser).isPresent(),
            () -> assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId()),
            () -> assertThat(foundUser.get().getRefreshToken()).isEqualTo(savedUser.getRefreshToken())
        );
    }

    @Test
    @DisplayName("소셜 타입과 소셜 식별자로 사용자를 가져온다.")
    void findBySocialTypeAndSocialId() {
        // given
        User savedUser = userRepository.save(UserTestData.createUser());

        // when
        Optional<User> foundUser = userRepository.findBySocialTypeAndSocialId(savedUser.getSocialType(), savedUser.getSocialId());

        // then
        assertAll(
            () -> assertThat(foundUser).isPresent(),
            () -> assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId()),
            () -> assertThat(foundUser.get().getSocialType()).isEqualTo(savedUser.getSocialType()),
            () -> assertThat(foundUser.get().getSocialId()).isEqualTo(savedUser.getSocialId())
        );
    }
}