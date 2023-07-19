package kr.mybrary.userservice.user.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import kr.mybrary.userservice.user.UserFixture;
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
        User savedUser = userRepository.save(UserFixture.COMMON_USER.getUser());

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
        User savedUser = userRepository.save(UserFixture.COMMON_USER.getUser());

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
        User savedUser = userRepository.save(UserFixture.COMMON_USER.getUser());

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
    @DisplayName("로그인 아이디가 존재하는지 확인한다.")
    void existsByLoginId() {
        // given
        User savedUser = userRepository.save(UserFixture.COMMON_USER.getUser());

        // when
        boolean exists = userRepository.existsByLoginId(savedUser.getLoginId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("이메일이 존재하는지 확인한다.")
    void existsByEmail() {
        // given
        User savedUser = userRepository.save(UserFixture.COMMON_USER.getUser());

        // when
        boolean exists = userRepository.existsByEmail(savedUser.getEmail());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("닉네임이 존재하는지 확인한다.")
    void existsByNickname() {
        // given
        User savedUser = userRepository.save(UserFixture.COMMON_USER.getUser());

        // when
        boolean exists = userRepository.existsByNickname(savedUser.getNickname());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("소셜 타입과 소셜 식별자로 사용자를 가져온다.")
    void findBySocialTypeAndSocialId() {
        // given
        User savedUser = userRepository.save(UserFixture.COMMON_USER.getUser());

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