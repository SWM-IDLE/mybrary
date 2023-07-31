package kr.mybrary.userservice.interest.domain;

import kr.mybrary.userservice.interest.InterestCategoryFixture;
import kr.mybrary.userservice.interest.InterestFixture;
import kr.mybrary.userservice.interest.UserInterestFixture;
import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryServiceResponse;
import kr.mybrary.userservice.interest.domain.dto.response.UserInterestServiceResponse;
import kr.mybrary.userservice.interest.persistence.InterestCategory;
import kr.mybrary.userservice.interest.persistence.repository.InterestCategoryRepository;
import kr.mybrary.userservice.interest.persistence.repository.UserInterestRepository;
import kr.mybrary.userservice.user.UserFixture;
import kr.mybrary.userservice.user.domain.UserService;
import kr.mybrary.userservice.user.domain.dto.response.UserResponse;
import kr.mybrary.userservice.user.domain.exception.user.UserNotFoundException;
import kr.mybrary.userservice.user.persistence.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InterestServiceImplTest {

    @Mock
    private InterestCategoryRepository interestCategoryRepository;
    @Mock
    private UserInterestRepository userInterestRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private InterestServiceImpl interestService;

    static final String LOGIN_ID = "loginId";

    @Test
    @DisplayName("관심사 목록을 카테고리별로 조회한다.")
    void getInterestsWithCategory() {
        // given
        InterestCategory literatureSensibility = InterestCategoryFixture.LITERATURE_SENSIBILITY.getInterestCategory();
        InterestCategory learningGrowth = InterestCategoryFixture.LEARNING_GROWTH.getInterestCategory();
        InterestCategory cultureHistory = InterestCategoryFixture.CULTURE_HISTORY.getInterestCategory();

        given(interestCategoryRepository.findAllWithInterestUsingFetchJoin()).willReturn(
                List.of(literatureSensibility, learningGrowth, cultureHistory));

        // when
        InterestCategoryServiceResponse interestCategoryServiceResponse = interestService.getInterestCategories();

        // then
        assertAll(
                () -> assertThat(interestCategoryServiceResponse.getInterestCategories()).hasSize(3),
                () -> assertThat(interestCategoryServiceResponse.getInterestCategories().get(0).getName()).isEqualTo(literatureSensibility.getName()),
                () -> assertThat(interestCategoryServiceResponse.getInterestCategories().get(1).getName()).isEqualTo(learningGrowth.getName()),
                () -> assertThat(interestCategoryServiceResponse.getInterestCategories().get(2).getName()).isEqualTo(cultureHistory.getName()),
                () -> assertThat(interestCategoryServiceResponse.getInterestCategories().get(0).getDescription()).isEqualTo(literatureSensibility.getDescription()),
                () -> assertThat(interestCategoryServiceResponse.getInterestCategories().get(1).getDescription()).isEqualTo(learningGrowth.getDescription()),
                () -> assertThat(interestCategoryServiceResponse.getInterestCategories().get(2).getDescription()).isEqualTo(cultureHistory.getDescription()),
                () -> assertThat(interestCategoryServiceResponse.getInterestCategories().get(0).getInterestResponses()).hasSize(3),
                () -> assertThat(interestCategoryServiceResponse.getInterestCategories().get(1).getInterestResponses()).hasSize(2),
                () -> assertThat(interestCategoryServiceResponse.getInterestCategories().get(2).getInterestResponses()).hasSize(1),
                () -> assertThat(interestCategoryServiceResponse.getInterestCategories().get(0).getInterestResponses()).extracting("name").containsExactly(
                        InterestFixture.DOMESTIC_NOVEL.getInterest().getName(), InterestFixture.FOREIGN_NOVEL.getInterest().getName(), InterestFixture.POEM.getInterest().getName()),
                () -> assertThat(interestCategoryServiceResponse.getInterestCategories().get(1).getInterestResponses()).extracting("name").containsExactly(
                        InterestFixture.SELF_IMPROVEMENT.getInterest().getName(), InterestFixture.SCIENCE.getInterest().getName()),
                () -> assertThat(interestCategoryServiceResponse.getInterestCategories().get(2).getInterestResponses()).extracting("name").containsExactly(
                        InterestFixture.KOREAN_HISTORY.getInterest().getName())
        );

        verify(interestCategoryRepository).findAllWithInterestUsingFetchJoin();
    }

    @Test
    @DisplayName("사용자의 관삼사를 모두 조회한다.")
    void getUserInterests() {
        // given
        User user = UserFixture.COMMON_USER.getUser();
        given(userService.getUserResponse(LOGIN_ID)).willReturn(UserResponse.builder().user(user).build());
        given(userInterestRepository.findAllByUserWithInterestUsingFetchJoin(user)).willReturn(
                List.of(UserInterestFixture.COMMON_USER_INTEREST_1.getUserInterest(),
                        UserInterestFixture.COMMON_USER_INTEREST_2.getUserInterest()));

        // when
        UserInterestServiceResponse userInterests = interestService.getUserInterests(LOGIN_ID);

        // then
        assertAll(
                () -> assertThat(userInterests.getUserInterests()).hasSize(2),
                () -> assertThat(userInterests.getUserInterests()).extracting("name").containsExactly(
                        UserInterestFixture.COMMON_USER_INTEREST_1.getUserInterest().getInterest().getName(),
                        UserInterestFixture.COMMON_USER_INTEREST_2.getUserInterest().getInterest().getName())
        );

        verify(userService).getUserResponse(LOGIN_ID);
        verify(userInterestRepository).findAllByUserWithInterestUsingFetchJoin(user);
    }

    @Test
    @DisplayName("사용자의 관심사를 모두 조회할 때 사용자가 존재하지 않으면 예외가 발생한다.")
    void getUserInterestsWithNotExistUser() {
        // given
         given(userService.getUserResponse(LOGIN_ID)).willThrow(new UserNotFoundException());

        // when then
        assertThatThrownBy(() -> interestService.getUserInterests(LOGIN_ID))
                .isInstanceOf(UserNotFoundException.class)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue("errorCode", "U-05")
                .hasFieldOrPropertyWithValue("errorMessage", "존재하지 않는 사용자입니다.");

        verify(userService).getUserResponse(LOGIN_ID);
    }

    @Test
    @DisplayName("사용자의 관심사를 모두 조회할 때 사용자의 관심사가 없으면 빈 목록을 반환한다.")
    void getUserInterestsWithEmptyUserInterest() {
        // given
        User user = UserFixture.COMMON_USER.getUser();
        given(userService.getUserResponse(LOGIN_ID)).willReturn(UserResponse.builder().user(user).build());
        given(userInterestRepository.findAllByUserWithInterestUsingFetchJoin(user)).willReturn(List.of());

        // when
        UserInterestServiceResponse userInterests = interestService.getUserInterests(LOGIN_ID);

        // then
        assertThat(userInterests.getUserInterests()).isEmpty();

        verify(userService).getUserResponse(LOGIN_ID);
        verify(userInterestRepository).findAllByUserWithInterestUsingFetchJoin(user);
    }

}