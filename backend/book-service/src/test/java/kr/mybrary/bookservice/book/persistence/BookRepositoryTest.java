package kr.mybrary.bookservice.book.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.persistence.bookInfo.Author;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookAuthor;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookCategory;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookTranslator;
import kr.mybrary.bookservice.book.persistence.bookInfo.Translator;
import org.hibernate.proxy.HibernateProxy;
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

    @DisplayName("isbn13으로 도서를 조회한다.")
    @Test
    void findByIsbn13() {

        // given
        Book book = BookFixture.COMMON_BOOK.getBook();

        // when
        bookRepository.save(book);

        entityManager.flush();
        entityManager.clear();

        // then
        assertAll(
                () -> assertThat(bookRepository.findByIsbn13(book.getIsbn13()).isPresent()).isTrue()
        );
    }

    @DisplayName("isbn13으로 도서를 조회시, 도서 저자, 도서 번역가, 도서 카테고리를 함께 조회한다.")
    @Test
    void findByISBN13WithAllDetailUsingFetchJoin() {

        // given
        Author author = entityManager.persist(Author.builder().aid(11).name("테스트 저자").build());
        Translator translator = entityManager.persist(Translator.builder().tid(12).name("테스트 번역가").build());
        BookCategory bookCategory = entityManager.persist(BookCategory.builder().cid(13).name("테스트 카테고리").build());

        Book book = entityManager.persist(BookFixture.COMMON_BOOK.getBookBuilder()
                .id(null)
                .bookTranslators(null)
                .bookAuthors(null)
                .bookCategory(bookCategory)
                .build());

        entityManager.persist(BookTranslator.builder().book(book).translator(translator).build());
        entityManager.persist(BookAuthor.builder().book(book).author(author).build());

        entityManager.flush();
        entityManager.clear();

        // when
        Optional<Book> foundBook = bookRepository.findByISBNWithAuthorAndCategoryUsingFetchJoin(book.getIsbn10(), book.getIsbn13());

        // then
        assertAll(
                () -> {
                    assertThat(foundBook).isNotEmpty();
                    assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
                    assertThat(foundBook.get().getBookCategory() instanceof HibernateProxy).isFalse();
                    assertThat(foundBook.get().getBookAuthors().get(0).getAuthor() instanceof HibernateProxy).isFalse();
                    assertThat(foundBook.get().getBookTranslators().get(0).getTranslator() instanceof HibernateProxy).isTrue();
                }
        );
    }
}