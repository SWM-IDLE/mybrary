package kr.mybrary.bookservice.book.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    TestEntityManager entityManager;

    @DisplayName("isbn10 또는 isbn13으로 도서를 조회한다.")
    @Test
    void findByIsbn10OrIsbn13() {

        // given
        Book book = BookFixture.COMMON_BOOK.getBook();

        // when
        bookRepository.save(book);

        entityManager.flush();
        entityManager.clear();

        // then
        assertAll(
                () -> assertThat(bookRepository.findByIsbn10OrIsbn13(book.getIsbn10(), "").isPresent()).isTrue(),
                () -> assertThat(bookRepository.findByIsbn10OrIsbn13("", book.getIsbn13()).isPresent()).isTrue()
        );
    }
}