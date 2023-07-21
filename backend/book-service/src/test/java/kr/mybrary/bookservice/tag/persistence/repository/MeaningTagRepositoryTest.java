package kr.mybrary.bookservice.tag.persistence.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import kr.mybrary.bookservice.tag.MeaningTagFixture;
import kr.mybrary.bookservice.tag.persistence.MeaningTag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MeaningTagRepositoryTest {

    @Autowired
    private MeaningTagRepository meaningTagRepository;

    @DisplayName("의미 태그 문구로 의미 태그를 조회한다.")
    @Test
    void findByMeaningTag() {

        // given
        String quote = "의미있는 책";
        meaningTagRepository.save(MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTag());

        // when
        Optional<MeaningTag> findTag = meaningTagRepository.findByQuote(quote);

        // when, then
        assertAll(
                () -> {
                    assertThat(findTag.isPresent()).isTrue();
                    assertThat(findTag.get().getQuote()).isEqualTo(quote);
                }
        );
    }
}