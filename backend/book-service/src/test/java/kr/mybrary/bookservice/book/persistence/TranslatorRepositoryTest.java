package kr.mybrary.bookservice.book.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import kr.mybrary.bookservice.book.persistence.repository.TranslatorRepository;
import kr.mybrary.bookservice.book.persistence.translator.Translator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TranslatorRepositoryTest {

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

        // when
        Translator foundTranslator = translatorRepository.findByName(savedTranslator.getName()).orElseThrow();

        // then
        assertThat(foundTranslator.getName()).isEqualTo(savedTranslator.getName());
    }

    private Translator createTranslator() {
        return Translator.builder()
                .name("translator_name")
                .build();
    }
}