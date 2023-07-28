package kr.mybrary.userservice.interest.domain;

import kr.mybrary.userservice.interest.InterestCategoryFixture;
import kr.mybrary.userservice.interest.InterestFixture;
import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryServiceResponse;
import kr.mybrary.userservice.interest.persistence.InterestCategory;
import kr.mybrary.userservice.interest.persistence.repository.InterestCategoryRepository;
import kr.mybrary.userservice.interest.persistence.repository.InterestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InterestServiceImplTest {

    @Mock
    private InterestRepository interestRepository;
    @Mock
    private InterestCategoryRepository interestCategoryRepository;

    @InjectMocks
    private InterestServiceImpl interestService;

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

}