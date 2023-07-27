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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

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

        given(interestCategoryRepository.findAll()).willReturn(
                List.of(literatureSensibility, learningGrowth, cultureHistory));
        given(interestRepository.findAllByCategory(literatureSensibility)).willReturn(
                List.of(InterestFixture.DOMESTIC_NOVEL.getInterestBuilder().category(literatureSensibility).build(),
                        InterestFixture.FOREIGN_NOVEL.getInterestBuilder().category(literatureSensibility).build(),
                        InterestFixture.POEM.getInterestBuilder().category(literatureSensibility).build()));
        given(interestRepository.findAllByCategory(learningGrowth)).willReturn(
                List.of(InterestFixture.SELF_IMPROVEMENT.getInterestBuilder().category(learningGrowth).build(),
                        InterestFixture.SCIENCE.getInterestBuilder().category(learningGrowth).build()));
        given(interestRepository.findAllByCategory(cultureHistory)).willReturn(
                    List.of(InterestFixture.KOREAN_HISTORY.getInterestBuilder().category(cultureHistory).build()));

        // when
        InterestCategoryServiceResponse interestCategoryServiceResponse = interestService.getInterestsGroupByCategory();

        // then
        assertAll(
                () -> assertEquals(3, interestCategoryServiceResponse.getInterestsGroupByCategory().size()),
                () -> assertThat(interestCategoryServiceResponse.getInterestsGroupByCategory().keySet())
                        .containsExactlyInAnyOrder(literatureSensibility, learningGrowth, cultureHistory),
                () -> assertEquals(3, interestCategoryServiceResponse. getInterestsGroupByCategory()
                        .get(literatureSensibility).size()),
                () -> assertEquals(2, interestCategoryServiceResponse.getInterestsGroupByCategory()
                        .get(learningGrowth).size()),
                () -> assertEquals(1, interestCategoryServiceResponse.getInterestsGroupByCategory()
                        .get(cultureHistory).size())
        );

    }

}