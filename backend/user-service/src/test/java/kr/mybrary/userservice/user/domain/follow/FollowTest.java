package kr.mybrary.userservice.user.domain.follow;

import kr.mybrary.userservice.user.domain.UserServiceImpl;
import kr.mybrary.userservice.user.domain.dto.request.FollowServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.FollowerServiceRequest;
import kr.mybrary.userservice.user.domain.dto.response.FollowStatusServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.FollowerServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.FollowingServiceResponse;
import kr.mybrary.userservice.user.domain.exception.follow.DuplicateFollowException;
import kr.mybrary.userservice.user.domain.exception.follow.SameSourceTargetUserException;
import kr.mybrary.userservice.user.domain.exception.user.UserNotFoundException;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.persistence.model.FollowUserInfoModel;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FollowTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    static User userA = User.builder()
            .id(1L)
            .loginId("userA")
            .followers(new ArrayList<>())
            .followings(new ArrayList<>())
            .build();

    static User userB = User.builder()
            .id(2L)
            .loginId("userB")
            .followers(new ArrayList<>())
            .followings(new ArrayList<>())
            .build();

    static User userC = User.builder()
            .id(3L)
            .loginId("userC")
            .followers(new ArrayList<>())
            .followings(new ArrayList<>())
            .build();

    @Test
    @Order(1)
    @DisplayName("userA는 userB를 팔로우한다.")
    void userAFollowUserB() {
        // given
        given(userRepository.findByLoginId(userA.getLoginId())).willReturn(Optional.of(userA));
        given(userRepository.findByLoginId(userB.getLoginId())).willReturn(Optional.of(userB));

        // when
        userService.follow(FollowServiceRequest.of(userA.getLoginId(), userB.getLoginId()));

        // then
        assertAll(
                () -> assertThat(userA.getFollowings()).hasSize(1),
                () -> assertThat(userA.getFollowings().get(0).getSource()).isEqualTo(userA),
                () -> assertThat(userA.getFollowings().get(0).getTarget()).isEqualTo(userB),
                () -> assertThat(userB.getFollowers()).hasSize(1),
                () -> assertThat(userB.getFollowers().get(0).getSource()).isEqualTo(userA),
                () -> assertThat(userB.getFollowers().get(0).getTarget()).isEqualTo(userB)
        );

        verify(userRepository).findByLoginId(userA.getLoginId());
        verify(userRepository).findByLoginId(userB.getLoginId());
    }

    @Test
    @Order(2)
    @DisplayName("userA의 팔로잉 목록을 조회 하면 1명(userB)이 조회된다.")
    void getUserAFollowings() {
        // given
        given(userRepository.findByLoginId(userA.getLoginId())).willReturn(Optional.of(userA));

        given(userRepository.findAllFollowings(userA.getId())).willReturn(
                Arrays.asList(FollowUserInfoModel.builder()
                                .loginId(userB.getLoginId())
                                .nickname(userB.getNickname())
                                .profileImageUrl(userB.getProfileImageUrl())
                                .build()));

        // when
        FollowingServiceResponse response = userService.getFollowings(userA.getLoginId());

        // then
        assertAll(
                () -> assertThat(response.getUserId()).isEqualTo(userA.getLoginId()),
                () -> assertThat(response.getFollowings()).hasSize(1),
                () -> assertThat(response.getFollowings()).extracting("userId").contains(userB.getLoginId())
        );

        verify(userRepository).findByLoginId(userA.getLoginId());
        verify(userRepository).findAllFollowings(userA.getId());
    }

    @Test
    @Order(3)
    @DisplayName("userA가 userB를 팔로우 중인지 확인한다.")
    void checkUserAFollowingUserBTrue() {
        // given
        given(userRepository.findByLoginId(userA.getLoginId())).willReturn(Optional.of(userA));
        given(userRepository.findByLoginId(userB.getLoginId())).willReturn(Optional.of(userB));

        // when
        FollowStatusServiceResponse response = userService.getFollowStatus(FollowServiceRequest.of(userA.getLoginId(), userB.getLoginId()));

        // then
        assertAll(
                () -> assertThat(response.getUserId()).isEqualTo(userA.getLoginId()),
                () -> assertThat(response.getTargetId()).isEqualTo(userB.getLoginId()),
                () -> assertThat(response.isFollowing()).isTrue()
        );
    }

    @Test
    @Order(4)
    @DisplayName("userC는 userB를 팔로우한다.")
    void userCFollowUserB() {
        // given
        given(userRepository.findByLoginId(userC.getLoginId())).willReturn(Optional.of(userC));
        given(userRepository.findByLoginId(userB.getLoginId())).willReturn(Optional.of(userB));

        // when
        userService.follow(FollowServiceRequest.of(userC.getLoginId(), userB.getLoginId()));

        // then
        assertAll(
                () -> assertThat(userC.getFollowings()).hasSize(1),
                () -> assertThat(userC.getFollowings().get(0).getSource()).isEqualTo(userC),
                () -> assertThat(userC.getFollowings().get(0).getTarget()).isEqualTo(userB),
                () -> assertThat(userB.getFollowers()).hasSize(2),
                () -> assertThat(userB.getFollowers().get(1).getSource()).isEqualTo(userC),
                () -> assertThat(userB.getFollowers().get(1).getTarget()).isEqualTo(userB)
        );

        verify(userRepository).findByLoginId(userC.getLoginId());
        verify(userRepository).findByLoginId(userB.getLoginId());
    }

    @Test
    @Order(5)
    @DisplayName("userC가 userB를 다시 팔로우하면 예외가 발생한다.")
    void userCFollowUserBAgain() {
        // given
        given(userRepository.findByLoginId(userC.getLoginId())).willReturn(Optional.of(userC));
        given(userRepository.findByLoginId(userB.getLoginId())).willReturn(Optional.of(userB));

        // when then
        assertThatThrownBy(() -> userService.follow(FollowServiceRequest.of(userC.getLoginId(), userB.getLoginId())))
                .isInstanceOf(DuplicateFollowException.class)
                .hasFieldOrPropertyWithValue("status", 400)
                .hasFieldOrPropertyWithValue("errorCode", "F-02")
                .hasFieldOrPropertyWithValue("errorMessage", "이미 팔로우한 사용자입니다.");

        verify(userRepository).findByLoginId(userC.getLoginId());
        verify(userRepository).findByLoginId(userB.getLoginId());
    }

    @Test
    @Order(6)
    @DisplayName("userB의 팔로워 목록을 조회하면 2명(userA, userC)이 조회된다.")
    void getUserBFollowers() {
        // given
        given(userRepository.findByLoginId(userB.getLoginId())).willReturn(Optional.of(userB));

        given(userRepository.findAllFollowers(userB.getId())).willReturn(
                Arrays.asList(FollowUserInfoModel.builder()
                                .loginId(userA.getLoginId())
                                .nickname(userA.getNickname())
                                .profileImageUrl(userA.getProfileImageUrl())
                                .build(),
                        FollowUserInfoModel.builder()
                                .loginId(userC.getLoginId())
                                .nickname(userC.getNickname())
                                .profileImageUrl(userC.getProfileImageUrl())
                                .build()));

        // when
        FollowerServiceResponse response = userService.getFollowers(userB.getLoginId());

        // then
        assertAll(
                () -> assertThat(response.getUserId()).isEqualTo(userB.getLoginId()),
                () -> assertThat(response.getFollowers()).hasSize(2),
                () -> assertThat(response.getFollowers()).extracting("userId").containsExactly(userA.getLoginId(), userC.getLoginId())
        );

        verify(userRepository).findByLoginId(userB.getLoginId());
        verify(userRepository).findAllFollowers(userB.getId());
    }

    @Test
    @Order(7)
    @DisplayName("userA는 userB를 언팔로우한다.")
    void userAUnfollowUserB() {
        // given
        given(userRepository.findByLoginId(userA.getLoginId())).willReturn(Optional.of(userA));
        given(userRepository.findByLoginId(userB.getLoginId())).willReturn(Optional.of(userB));

        // when
        userService.unfollow(FollowServiceRequest.of(userA.getLoginId(), userB.getLoginId()));

        // then
        assertAll(
                () -> assertThat(userA.getFollowings()).hasSize(0),
                () -> assertThat(userB.getFollowers()).hasSize(1)
        );

        verify(userRepository).findByLoginId(userA.getLoginId());
        verify(userRepository).findByLoginId(userB.getLoginId());
    }

    @Test
    @Order(8)
    @DisplayName("userA가 userB를 팔로우 중이지 않은지 확인한다.")
    void checkUserAFollowingUserBFalse() {
        // given
        given(userRepository.findByLoginId(userA.getLoginId())).willReturn(Optional.of(userA));
        given(userRepository.findByLoginId(userB.getLoginId())).willReturn(Optional.of(userB));

        // when
        FollowStatusServiceResponse response = userService.getFollowStatus(FollowServiceRequest.of(userA.getLoginId(), userB.getLoginId()));

        // then
        assertAll(
                () -> assertThat(response.getUserId()).isEqualTo(userA.getLoginId()),
                () -> assertThat(response.getTargetId()).isEqualTo(userB.getLoginId()),
                () -> assertThat(response.isFollowing()).isFalse()
        );
    }

    @Test
    @Order(9)
    @DisplayName("userA의 팔로잉 목록을 조회 하면 0명이 조회된다.")
    void getUserAFollowingsAfterUnfollow() {
        // given
        given(userRepository.findByLoginId(userA.getLoginId())).willReturn(Optional.of(userA));

        // when
        FollowingServiceResponse response = userService.getFollowings(userA.getLoginId());

        // then
        assertAll(
                () -> assertThat(response.getUserId()).isEqualTo(userA.getLoginId()),
                () -> assertThat(response.getFollowings()).hasSize(0)
        );

        verify(userRepository).findByLoginId(userA.getLoginId());
    }

    @Test
    @Order(10)
    @DisplayName("userB는 userC를 팔로워 목록에서 삭제한다.")
    void userBDeletefollowerUserC() {
        // given
        given(userRepository.findByLoginId(userB.getLoginId())).willReturn(Optional.of(userB));
        given(userRepository.findByLoginId(userC.getLoginId())).willReturn(Optional.of(userC));

        // when
        userService.deleteFollower(FollowerServiceRequest.of(userB.getLoginId(), userC.getLoginId()));

        // then
        assertAll(
                () -> assertThat(userB.getFollowers()).hasSize(0),
                () -> assertThat(userC.getFollowings()).hasSize(0)
        );

        verify(userRepository).findByLoginId(userB.getLoginId());
        verify(userRepository).findByLoginId(userC.getLoginId());
    }

    @Test
    @Order(11)
    @DisplayName("userB의 팔로워 목록을 조회하면 0명이 조회된다.")
    void getUserBFollowersAfterFollowerDelete() {
        // given
        given(userRepository.findByLoginId(userB.getLoginId())).willReturn(Optional.of(userB));

        // when
        FollowerServiceResponse response = userService.getFollowers(userB.getLoginId());

        // then
        assertAll(
                () -> assertThat(response.getUserId()).isEqualTo(userB.getLoginId()),
                () -> assertThat(response.getFollowers()).hasSize(0)
        );

        verify(userRepository).findByLoginId(userB.getLoginId());
    }

    @Test
    @Order(12)
    @DisplayName("userA가 userA 자신을 팔로우하면 예외가 발생한다.")
    void userAFollowUserA() {
        // given
        given(userRepository.findByLoginId(userA.getLoginId())).willReturn(Optional.of(userA));

        // when then
        assertThatThrownBy(() -> userService.follow(FollowServiceRequest.of(userA.getLoginId(), userA.getLoginId())))
                .isInstanceOf(SameSourceTargetUserException.class)
                .hasFieldOrPropertyWithValue("status", 400)
                .hasFieldOrPropertyWithValue("errorCode", "F-01")
                .hasFieldOrPropertyWithValue("errorMessage", "자기 자신을 팔로우 또는 언팔로우할 수 없습니다.");

        verify(userRepository, times(2)).findByLoginId(userA.getLoginId());
    }

    @Test
    @Order(13)
    @DisplayName("userA가 없는 사용자를 팔로우하면 예외가 발생한다.")
    void userAFollowNonExistUser() {
        // given
        given(userRepository.findByLoginId(userA.getLoginId())).willReturn(Optional.of(userA));
        given(userRepository.findByLoginId("nonExistUser")).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> userService.follow(FollowServiceRequest.of(userA.getLoginId(), "nonExistUser")))
                .isInstanceOf(UserNotFoundException.class)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue("errorCode", "U-05")
                .hasFieldOrPropertyWithValue("errorMessage", "존재하지 않는 사용자입니다.");

        verify(userRepository).findByLoginId(userA.getLoginId());
        verify(userRepository).findByLoginId("nonExistUser");
    }

}
