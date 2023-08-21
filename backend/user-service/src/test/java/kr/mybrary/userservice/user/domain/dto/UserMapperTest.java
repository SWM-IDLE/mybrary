package kr.mybrary.userservice.user.domain.dto;

import static org.junit.jupiter.api.Assertions.*;

import kr.mybrary.userservice.user.UserFixture;
import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.userservice.user.domain.dto.response.ProfileServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.SearchServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.SignUpServiceResponse;
import kr.mybrary.userservice.user.persistence.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserMapperTest {

    @Test
    @DisplayName("회원가입 요청 DTO를 User 엔티티로 변환한다.")
    void signUpServiceRequestToEntity() {
        // given
        SignUpServiceRequest serviceRequest = SignUpServiceRequest.builder()
            .loginId("test")
            .password("test")
            .email("eamil@mail.com")
            .nickname("nickname")
            .build();

        // when
        User user = UserMapper.INSTANCE.toEntity(serviceRequest);

        // then
        assertAll(
            () -> assertEquals(serviceRequest.getLoginId(), user.getLoginId()),
            () -> assertEquals(serviceRequest.getPassword(), user.getPassword()),
            () -> assertEquals(serviceRequest.getEmail(), user.getEmail()),
            () -> assertEquals(serviceRequest.getNickname(), user.getNickname())
        );
    }

    @Test
    @DisplayName("User 엔티티를 회원가입 응답 DTO로 변환한다.")
    void toSignUpServiceResponse() {
        // given
        User user = UserFixture.COMMON_USER.getUser();

        // when
        SignUpServiceResponse serviceResponse = UserMapper.INSTANCE.toSignUpServiceResponse(user);

        // then
        assertAll(
            () -> assertEquals(user.getLoginId(), serviceResponse.getLoginId()),
            () -> assertEquals(user.getEmail(), serviceResponse.getEmail()),
            () -> assertEquals(user.getNickname(), serviceResponse.getNickname()),
            () -> assertEquals(user.getRole(), serviceResponse.getRole())
        );
    }

    @Test
    @DisplayName("User 엔티티를 프로필 응답 DTO로 변환한다.")
    void toProfileServiceResponse() {
        // given
        User user = UserFixture.COMMON_USER.getUser();

        // when
        ProfileServiceResponse serviceResponse = UserMapper.INSTANCE.toProfileServiceResponse(user);

        // then
        assertAll(
            () -> assertEquals(user.getNickname(), serviceResponse.getNickname()),
            () -> assertEquals(user.getProfileImageUrl(), serviceResponse.getProfileImageUrl()),
            () -> assertEquals(user.getIntroduction(), serviceResponse.getIntroduction())
        );
    }
}