package kr.mybrary.bookservice.tag.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import kr.mybrary.bookservice.PersistenceTest;
import kr.mybrary.bookservice.tag.MeaningTagFixture;
import kr.mybrary.bookservice.tag.persistence.repository.MeaningTagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@PersistenceTest
class MeaningTagRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private MeaningTagRepository meaningTagRepository;

    @DisplayName("의미 태그 문구로 의미 태그를 조회한다.")
    @Test
    void findByMeaningTag() {

        // given
        String quote = "의미있는 책";
        meaningTagRepository.save(MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTag());

        // when
        Optional<MeaningTag> foundTag = meaningTagRepository.findByQuote(quote);

        entityManager.flush();
        entityManager.clear();

        // when, then
        assertAll(
                () -> {
                    assertThat(foundTag.isPresent()).isTrue();
                    assertThat(foundTag.get().getQuote()).isEqualTo(quote);
                }
        );
    }

    @DisplayName("가장 많이 등록된 의미 태그를 순서로 페이징 조회한다.")
    @Test
    void findPageOrderByRegisteredCountDesc() {

        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        for (int i = 1; i <= 20; i++) {
            meaningTagRepository.save(MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTagBuilder()
                    .id((long) i)
                    .registeredCount(i).build());
        }

        entityManager.flush();
        entityManager.clear();

        // when
        Page<MeaningTag> result = meaningTagRepository.findAllByOrderByRegisteredCountDesc(pageRequest);

        // then
        assertAll(
                () -> assertThat(result.getContent().size()).isEqualTo(10),
                () -> assertThat(result.getContent().get(0).getRegisteredCount()).isEqualTo(20),
                () -> assertThat(result.getContent().get(9).getRegisteredCount()).isEqualTo(11)
        );
    }
}