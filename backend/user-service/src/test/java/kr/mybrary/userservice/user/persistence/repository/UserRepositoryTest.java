package kr.mybrary.userservice.user.persistence.repository;

import kr.mybrary.userservice.PersistenceTest;
import kr.mybrary.userservice.user.UserFixture;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.persistence.model.FollowUserInfoModel;
import kr.mybrary.userservice.user.persistence.model.UserInfoModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@PersistenceTest
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

    @Test
    @DisplayName("닉네임에 해당 문자열을 포함하는 사용자를 가져온다.")
    void findByNicknameContaining() {
        // given
        User savedUser = userRepository.save(UserFixture.COMMON_USER.getUser());
        User savedUser2 = userRepository.save(UserFixture.USER_WITH_SIMILAR_NICKNAME.getUser());

        // when
        List<User> foundUser = userRepository.findByNicknameContaining(savedUser.getNickname());

        // then
        assertAll(
            () -> assertThat(foundUser).hasSize(2),
            () -> assertThat(foundUser.get(0).getId()).isEqualTo(savedUser.getId()),
            () -> assertThat(foundUser.get(0).getNickname()).contains(savedUser.getNickname()),
            () -> assertThat(foundUser.get(1).getId()).isEqualTo(savedUser2.getId()),
            () -> assertThat(foundUser.get(1).getNickname()).contains(savedUser.getNickname())
        );
    }

    @Test
    @DisplayName("로그인 아이디 목록으로 사용자 정보를 가져온다.")
    void findAllUserInfoByLoginIds() {
        // given
        User savedUser = userRepository.save(UserFixture.COMMON_USER.getUser());
        User savedUser2 = userRepository.save(UserFixture.USER_WITH_SIMILAR_NICKNAME.getUser());

        // when
        List<UserInfoModel> userInfos = userRepository.findAllUserInfoByLoginIds(List.of(savedUser.getLoginId(), savedUser2.getLoginId()));

        // then
        assertAll(
            () -> assertThat(userInfos).hasSize(2),
            () -> assertThat(userInfos).extracting("loginId").containsExactly(savedUser.getLoginId(), savedUser2.getLoginId()),
            () -> assertThat(userInfos).extracting("nickname").containsExactly(savedUser.getNickname(), savedUser2.getNickname()),
            () -> assertThat(userInfos).extracting("profileImageUrl").containsExactly(savedUser.getProfileImageUrl(), savedUser2.getProfileImageUrl())
        );
    }

    @Test
    @DisplayName("사용자 아이디로 팔로잉 사용자 정보 목록을 가져온다.")
    void findAllFollowings() {
        // given
        User user = User.builder()
            .loginId("loginId")
            .nickname("nickname")
            .profileImageUrl("profileImageUrl")
            .password("password")
            .followings(new ArrayList<>())
            .build();
        User followingUser = User.builder()
            .loginId("followingLoginId")
            .nickname("followingNickname")
            .profileImageUrl("followingProfileImageUrl")
            .password("password")
            .followers(new ArrayList<>())
            .build();

        userRepository.save(followingUser);
        user.follow(followingUser);
        User savedUser = userRepository.save(user);

        // when
        List<FollowUserInfoModel> followUserInfoModels = userRepository.findAllFollowings(savedUser.getId());

        // then
        assertAll(
            () -> assertThat(followUserInfoModels).hasSize(1),
            () -> assertThat(followUserInfoModels).extracting("loginId").containsExactly(followingUser.getLoginId()),
            () -> assertThat(followUserInfoModels).extracting("nickname").containsExactly(followingUser.getNickname()),
            () -> assertThat(followUserInfoModels).extracting("profileImageUrl").containsExactly(followingUser.getProfileImageUrl())
        );
    }

    @Test
    @DisplayName("사용자 아이디로 팔로워 사용자 정보 목록을 가져온다.")
    void findAllFollowers() {
        // given
        User user = User.builder()
            .loginId("loginId")
            .nickname("nickname")
            .profileImageUrl("profileImageUrl")
            .password("password")
            .followers(new ArrayList<>())
            .build();
        User followerUser = User.builder()
            .loginId("followerLoginId")
            .nickname("followerNickname")
            .profileImageUrl("followerProfileImageUrl")
            .password("password")
            .followings(new ArrayList<>())
            .build();

        userRepository.save(followerUser);
        followerUser.follow(user);
        User savedUser = userRepository.save(user);

        // when
        List<FollowUserInfoModel> followUserInfoModels = userRepository.findAllFollowers(savedUser.getId());

        // then
        assertAll(
            () -> assertThat(followUserInfoModels).hasSize(1),
            () -> assertThat(followUserInfoModels).extracting("loginId").containsExactly(followerUser.getLoginId()),
            () -> assertThat(followUserInfoModels).extracting("nickname").containsExactly(followerUser.getNickname()),
            () -> assertThat(followUserInfoModels).extracting("profileImageUrl").containsExactly(followerUser.getProfileImageUrl())
        );
    }

}