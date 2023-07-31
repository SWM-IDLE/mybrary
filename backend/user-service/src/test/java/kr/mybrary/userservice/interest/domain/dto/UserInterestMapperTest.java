package kr.mybrary.userservice.interest.domain.dto;

import kr.mybrary.userservice.interest.UserInterestFixture;
import kr.mybrary.userservice.interest.domain.dto.response.InterestResponse;
import kr.mybrary.userservice.interest.persistence.UserInterest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserInterestMapperTest {

    @Test
    @DisplayName("UserInterest 엔티티를 관심사 응답 DTO로 변환한다.")
    void toInterestResponse() {
        // given
        UserInterest userInterest = UserInterestFixture.COMMON_USER_INTEREST_1.getUserInterest();

        // when
        InterestResponse interestResponse = UserInterestMapper.INSTANCE.toInterestResponse(userInterest);

        // then
        assertAll(
            () -> assertEquals(userInterest.getInterest().getName(), interestResponse.getName()),
            () -> assertEquals(userInterest.getInterest().getId(), interestResponse.getId())
        );
    }
}