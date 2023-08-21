package kr.mybrary.userservice.interest.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import kr.mybrary.userservice.client.book.api.BookServiceClient;
import kr.mybrary.userservice.interest.InterestCategoryFixture;
import kr.mybrary.userservice.interest.InterestDtoTestData;
import kr.mybrary.userservice.interest.InterestFixture;
import kr.mybrary.userservice.interest.UserInterestFixture;
import kr.mybrary.userservice.interest.domain.dto.request.UserInterestAndBookRecommendationsServiceRequest;
import kr.mybrary.userservice.interest.domain.dto.request.UserInterestUpdateServiceRequest;
import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryServiceResponse;
import kr.mybrary.userservice.interest.domain.dto.response.UserInterestAndBookRecommendationsResponse;
import kr.mybrary.userservice.interest.domain.dto.response.UserInterestServiceResponse;
import kr.mybrary.userservice.interest.domain.exception.DuplicateUserInterestUpdateRequestException;
import kr.mybrary.userservice.interest.domain.exception.InterestNotFoundException;
import kr.mybrary.userservice.interest.domain.exception.UserInterestUpdateRequestNotAuthenticated;
import kr.mybrary.userservice.interest.domain.exception.UserInterestUpdateRequestSizeExceededException;
import kr.mybrary.userservice.interest.persistence.InterestCategory;
import kr.mybrary.userservice.interest.persistence.UserInterest;
import kr.mybrary.userservice.interest.persistence.repository.InterestCategoryRepository;
import kr.mybrary.userservice.interest.persistence.repository.InterestRepository;
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

@ExtendWith(MockitoExtension.class)
class InterestServiceImplTest {

    @Mock
    private InterestCategoryRepository interestCategoryRepository;
    @Mock
    private UserInterestRepository userInterestRepository;
    @Mock
    private InterestRepository interestRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookServiceClient bookServiceClient;

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

    @Test
    @DisplayName("사용자의 관심사를 업데이트한다.")
    void updateUserInterests() {
        // given
        User user = UserFixture.COMMON_USER.getUser();
        given(userService.getUserResponse(LOGIN_ID)).willReturn(UserResponse.builder().user(user).build());
        doNothing().when(userInterestRepository).deleteAllByUser(user);
        doNothing().when(userInterestRepository).flush();
        given(interestRepository.findById(1L)).willReturn(Optional.of(InterestFixture.DOMESTIC_NOVEL.getInterest()));
        given(interestRepository.findById(2L)).willReturn(Optional.of(InterestFixture.FOREIGN_NOVEL.getInterest()));
        given(userInterestRepository.save(any(UserInterest.class))).willReturn(UserInterestFixture.COMMON_USER_INTEREST_1.getUserInterest(),
                UserInterestFixture.COMMON_USER_INTEREST_2.getUserInterest());
        given(userInterestRepository.findAllByUserWithInterestUsingFetchJoin(user)).willReturn(
                List.of(UserInterestFixture.COMMON_USER_INTEREST_1.getUserInterest(),
                        UserInterestFixture.COMMON_USER_INTEREST_2.getUserInterest()));
        UserInterestUpdateServiceRequest serviceRequest = UserInterestUpdateServiceRequest.builder()
                .loginId(LOGIN_ID)
                .userId(LOGIN_ID)
                .interestIds(List.of(1L, 2L))
                .build();

        // when
        UserInterestServiceResponse userInterestServiceResponse = interestService.updateUserInterests(serviceRequest);

        // then
        assertAll(
                () -> assertThat(userInterestServiceResponse.getUserInterests()).hasSize(2),
                () -> assertThat(userInterestServiceResponse.getUserInterests()).extracting("name").containsExactly(
                        UserInterestFixture.COMMON_USER_INTEREST_1.getUserInterest().getInterest().getName(),
                        UserInterestFixture.COMMON_USER_INTEREST_2.getUserInterest().getInterest().getName())
        );

        verify(userService, times(2)).getUserResponse(LOGIN_ID);
        verify(userInterestRepository).deleteAllByUser(user);
        verify(userInterestRepository).flush();
        verify(interestRepository, times(2)).findById(anyLong());
        verify(userInterestRepository, times(2)).save(any(UserInterest.class));
        verify(userInterestRepository).findAllByUserWithInterestUsingFetchJoin(user);
    }

    @Test
    @DisplayName("사용자의 관심사를 업데이트할 때 사용자가 존재하지 않으면 예외가 발생한다.")
    void updateUserInterestsWithNotExistUser() {
        // given
        UserInterestUpdateServiceRequest serviceRequest = UserInterestUpdateServiceRequest.builder()
                .loginId(LOGIN_ID)
                .userId(LOGIN_ID)
                .interestIds(List.of(1L, 2L))
                .build();
        given(userService.getUserResponse(LOGIN_ID)).willThrow(new UserNotFoundException());

        // when then
        assertThatThrownBy(() -> interestService.updateUserInterests(serviceRequest))
                .isInstanceOf(UserNotFoundException.class)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue("errorCode", "U-05")
                .hasFieldOrPropertyWithValue("errorMessage", "존재하지 않는 사용자입니다.");

        verify(userService).getUserResponse(LOGIN_ID);
    }

    @Test
    @DisplayName("사용자의 관심사를 업데이트할 때 관심사가 존재하지 않으면 예외가 발생한다.")
    void updateUserInterestsWithNotExistInterest() {
        // given
        User user = UserFixture.COMMON_USER.getUser();
        given(userService.getUserResponse(LOGIN_ID)).willReturn(UserResponse.builder().user(user).build());
        doNothing().when(userInterestRepository).deleteAllByUser(user);
        doNothing().when(userInterestRepository).flush();
        given(interestRepository.findById(1L)).willReturn(Optional.of(InterestFixture.DOMESTIC_NOVEL.getInterest()));
        given(interestRepository.findById(2L)).willReturn(Optional.empty());
        UserInterestUpdateServiceRequest serviceRequest = UserInterestUpdateServiceRequest.builder()
                .loginId(LOGIN_ID)
                .userId(LOGIN_ID)
                .interestIds(List.of(1L, 2L))
                .build();

        // when then
        assertThatThrownBy(() -> interestService.updateUserInterests(serviceRequest))
                .isInstanceOf(InterestNotFoundException.class)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue("errorCode", "I-01")
                .hasFieldOrPropertyWithValue("errorMessage", "관심사를 찾을 수 없습니다.");

        verify(userService).getUserResponse(LOGIN_ID);
        verify(userInterestRepository).deleteAllByUser(user);
        verify(userInterestRepository).flush();
        verify(interestRepository, times(2)).findById(anyLong());
    }

    @Test
    @DisplayName("사용자의 관심사를 업데이트할 때 로그인한 사용자와 업데이트할 사용자가 다르면 예외가 발생한다.")
    void updateUserInterestsWithDifferentUser() {
        // given
        UserInterestUpdateServiceRequest serviceRequest = UserInterestUpdateServiceRequest.builder()
                .loginId(LOGIN_ID)
                .userId("another")
                .interestIds(List.of(1L, 2L, 3L))
                .build();

        // when then
        assertThatThrownBy(() -> interestService.updateUserInterests(serviceRequest))
                .isInstanceOf(UserInterestUpdateRequestNotAuthenticated.class)
                .hasFieldOrPropertyWithValue("status", 403)
                .hasFieldOrPropertyWithValue("errorCode", "I-04")
                .hasFieldOrPropertyWithValue("errorMessage",
                        "관심사를 수정할 수 있는 권한이 없습니다. 로그인한 사용자와 관심사 수정을 요청한 사용자가 일치하지 않습니다.");

    }

    @Test
    @DisplayName("사용자의 관심사를 업데이트할 때 관심사가 3개보다 많으면 예외가 발생한다.")
    void updateUserInterestsWithRequestSizeExceeded() {
        // given
        UserInterestUpdateServiceRequest serviceRequest = UserInterestUpdateServiceRequest.builder()
                .loginId(LOGIN_ID)
                .userId(LOGIN_ID)
                .interestIds(List.of(1L, 2L, 3L, 4L))
                .build();

        // when then
        assertThatThrownBy(() -> interestService.updateUserInterests(serviceRequest))
                .isInstanceOf(UserInterestUpdateRequestSizeExceededException.class)
                .hasFieldOrPropertyWithValue("status", 400)
                .hasFieldOrPropertyWithValue("errorCode", "I-02")
                .hasFieldOrPropertyWithValue("errorMessage", "관심사는 최대 3개까지 설정할 수 있습니다.");
    }

    @Test
    @DisplayName("사용자의 관심사를 업데이트할 때 중복된 관심사가 있으면 예외가 발생한다.")
    void updateUserInterestsWithDuplicatedInterest() {
        // given
        UserInterestUpdateServiceRequest serviceRequest = UserInterestUpdateServiceRequest.builder()
                .loginId(LOGIN_ID)
                .userId(LOGIN_ID)
                .interestIds(List.of(1L, 1L, 2L))
                .build();

        // when then
        assertThatThrownBy(() -> interestService.updateUserInterests(serviceRequest))
                .isInstanceOf(DuplicateUserInterestUpdateRequestException.class)
                .hasFieldOrPropertyWithValue("status", 400)
                .hasFieldOrPropertyWithValue("errorCode", "I-03")
                .hasFieldOrPropertyWithValue("errorMessage", "관심사는 중복해서 설정할 수 없습니다.");
    }

    @Test
    @DisplayName("feignClient를 통해 사용자의 모든 관심사와 하나의 관심사의 도서 추천 목록을 조회한다.")
    void getInterestsAndBookRecommendations() {

        // given
        UserInterestAndBookRecommendationsServiceRequest request = UserInterestAndBookRecommendationsServiceRequest.builder()
                .loginId(LOGIN_ID)
                .type("bestseller")
                .page(1)
                .build();

        given(userService.getUserResponse(request.getLoginId())).willReturn(UserResponse.builder().user(
                UserFixture.COMMON_USER.getUser()).build());
        given(userInterestRepository.findAllByUserWithInterestUsingFetchJoin(any(User.class))).willReturn(
                List.of(UserInterestFixture.COMMON_USER_INTEREST_1.getUserInterest(),
                        UserInterestFixture.COMMON_USER_INTEREST_2.getUserInterest()));
        given(bookServiceClient.getBookListByCategoryId(anyString(), anyInt(), anyInt())).willReturn(
                InterestDtoTestData.createBookRecommendationsServiceResponse());

        // when
        UserInterestAndBookRecommendationsResponse response = interestService.getInterestsAndBookRecommendations(request);

        // then
        assertAll(
                () -> assertThat(response.getUserInterests().size()).isEqualTo(2),
                () -> assertThat(response.getBookRecommendations().size()).isEqualTo(10)
        );
    }

    @Test
    @DisplayName("feignClient를 통해 사용자의 모든 관심사와 하나의 관심사의 도서 추천 목록을 조회시, 관심사가 없으면 빈 목록을 반환한다.")
    void getInterestsAndBookRecommendations2() {

        // given
        UserInterestAndBookRecommendationsServiceRequest request = UserInterestAndBookRecommendationsServiceRequest.builder()
                .loginId(LOGIN_ID)
                .type("bestseller")
                .page(1)
                .build();

        given(userService.getUserResponse(request.getLoginId())).willReturn(UserResponse.builder().user(
                UserFixture.COMMON_USER.getUser()).build());
        given(userInterestRepository.findAllByUserWithInterestUsingFetchJoin(any(User.class))).willReturn(List.of());

        // when
        UserInterestAndBookRecommendationsResponse response = interestService.getInterestsAndBookRecommendations(request);

        // then
        assertAll(
                () -> assertThat(response.getUserInterests().size()).isEqualTo(0),
                () -> assertThat(response.getBookRecommendations().size()).isEqualTo(0)
        );
    }

}