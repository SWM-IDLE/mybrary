package kr.mybrary.userservice.interest.persistence.repository;

import kr.mybrary.userservice.PersistenceTest;
import kr.mybrary.userservice.interest.persistence.Interest;
import kr.mybrary.userservice.interest.persistence.InterestCategory;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@PersistenceTest
class InterestCategoryRepositoryTest {

    @Autowired
    private InterestCategoryRepository interestCategoryRepository;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("페치 조인을 사용하여 관심사 카테고리와 관심사를 가져온다.")
    void findAllWithInterestUsingFetchJoin() {
       InterestCategory interestCategory = interestCategoryRepository.save(InterestCategory.builder()
                .name("interestCategory1")
                .description("interestCategory1Description")
                .interests(new ArrayList<>())
                .build());

        Interest interest1 = interestRepository.save(Interest.builder()
                .name("interest1")
                .code(1010)
                .build());

        interest1.updateCategory(interestCategory);

        entityManager.flush();
        entityManager.clear();

        // when
        List<InterestCategory> foundInterestCategories = interestCategoryRepository.findAllWithInterestUsingFetchJoin();

        // then
        assertAll(
                () -> assertThat(foundInterestCategories).isNotNull(),
                () -> assertThat(foundInterestCategories.get(0).getId()).isEqualTo(interestCategory.getId()),
                () -> assertThat(foundInterestCategories.get(0).getName()).isEqualTo(interestCategory.getName()),
                () -> assertThat(foundInterestCategories.get(0).getDescription()).isEqualTo(interestCategory.getDescription()),
                () -> assertThat(foundInterestCategories.get(0).getInterests().size()).isEqualTo(interestCategory.getInterests().size()),
                () -> assertThat(foundInterestCategories.get(0).getInterests().get(0).getId()).isEqualTo(interestCategory.getInterests().get(0).getId()),
                () -> assertThat(foundInterestCategories.get(0).getInterests().get(0).getName()).isEqualTo(interestCategory.getInterests().get(0).getName()),
                () -> assertThat(foundInterestCategories.get(0).getInterests().get(0)).isNotInstanceOf(HibernateProxy.class)
        );
    }
}