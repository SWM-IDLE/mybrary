package kr.mybrary.userservice.interest.persistence.repository;

import kr.mybrary.userservice.PersistenceTest;
import kr.mybrary.userservice.interest.persistence.Interest;
import kr.mybrary.userservice.interest.persistence.InterestCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@PersistenceTest
class InterestRepositoryTest {

    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private InterestCategoryRepository interestCategoryRepository;

    @Test
    @DisplayName("관심사명으로 관심사를 가져온다.")
    void findByName() {
        // given
        Interest savedInterest = interestRepository.save(Interest.builder()
                .name("테스트 관심사")
                .build());

        // when
        Interest foundInterest = interestRepository.findByName(savedInterest.getName()).get();

        // then
        assertAll(
                () -> assertThat(foundInterest).isNotNull(),
                () -> assertThat(foundInterest.getId()).isEqualTo(savedInterest.getId()),
                () -> assertThat(foundInterest.getName()).isEqualTo(savedInterest.getName())
        );
    }

    @Test
    @DisplayName("카테고리에 해당하는 관심사를 가져온다.")
    void findAllByCategory() {
        // given
        InterestCategory interestCategory = interestCategoryRepository.save(InterestCategory.builder()
                .name("테스트 카테고리")
                .description("테스트 카테고리 설명")
                .build());
        Interest savedInterest = interestRepository.save(Interest.builder()
                .name("테스트 관심사")
                .category(interestCategory)
                .code(1010)
                .build());
        Interest savedInterest2 = interestRepository.save(Interest.builder()
                .name("테스트 관심사2")
                .category(interestCategory)
                .code(1011)
                .build());

        // when
        List<Interest> foundInterest = interestRepository.findAllByCategory(interestCategory);

        // then
        assertAll(
                () -> assertThat(foundInterest).isNotEmpty(),
                () -> assertThat(foundInterest).hasSize(2),
                () -> assertThat(foundInterest).contains(savedInterest, savedInterest2)
        );
    }

}