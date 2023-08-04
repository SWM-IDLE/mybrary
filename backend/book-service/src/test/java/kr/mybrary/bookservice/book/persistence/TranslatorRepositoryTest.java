package kr.mybrary.bookservice.book.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kr.mybrary.bookservice.PersistenceTest;
import kr.mybrary.bookservice.book.persistence.bookInfo.Translator;
import kr.mybrary.bookservice.book.persistence.repository.TranslatorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@PersistenceTest
class TranslatorRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    TranslatorRepository translatorRepository;

    @DisplayName("번역가를 저장한다.")
    @Test
    void saveTranslator() {

        // given
        Translator translator = createTranslator();

        // when
        Translator savedTranslator = translatorRepository.save(translator);

        // then
        assertThat(savedTranslator.getName()).isEqualTo(translator.getName());
    }

    @DisplayName("저자를 이름으로 조회한다.")
    @Test
    void findTranslatorByName() {

        // given
        Translator savedTranslator = translatorRepository.save(createTranslator());

        entityManager.flush();
        entityManager.clear();

        // when
        Translator foundTranslator = translatorRepository.findByName(savedTranslator.getName()).orElseThrow();

        // then
        assertThat(foundTranslator.getName()).isEqualTo(savedTranslator.getName());
    }

    @DisplayName("변역자의 식별 ID로 조회한다.")
    @Test
    void findTranslatorByTid() {

        // given
        Translator savedTranslator = translatorRepository.save(createTranslator());

        entityManager.flush();
        entityManager.clear();

        // when
        Translator foundTranslator = translatorRepository.findByTid(savedTranslator.getTid()).orElseThrow();

        // then
        assertAll(
                () -> assertThat(foundTranslator.getTid()).isEqualTo(savedTranslator.getTid()),
                () -> assertThat(foundTranslator.getName()).isEqualTo(savedTranslator.getName())
        );
    }

    private Translator createTranslator() {
        return Translator.builder()
                .tid(3344)
                .name("translator_name")
                .build();
    }
}