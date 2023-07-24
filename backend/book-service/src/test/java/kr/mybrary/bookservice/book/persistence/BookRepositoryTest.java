package kr.mybrary.bookservice.book.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.book.persistence.author.Author;
import kr.mybrary.bookservice.book.persistence.author.BookAuthor;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import kr.mybrary.bookservice.book.persistence.translator.BookTranslator;
import kr.mybrary.bookservice.book.persistence.translator.Translator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @DisplayName("도서를 영속화할 때, 도서저자, 도서번역가, 저자, 번역가도 영속화된다.")
    @Test
    void saveBook() {

        // given
        Book book = createBook();
        book.addBookAuthor(createAuthor());
        book.addBookTranslator(createTranslator());

        // when
        Book savedBook = bookRepository.save(book);

        // then
        assertAll(
                () -> assertThat(savedBook.getTitle()).isEqualTo(book.getTitle()),
                () -> assertThat(savedBook.getIsbn10()).isEqualTo(book.getIsbn10()),
                () -> assertThat(savedBook.getIsbn13()).isEqualTo(book.getIsbn13()),
                () -> assertThat(savedBook.getBookAuthors().stream().map(BookAuthor::getAuthor))
                        .hasSize(2)
                        .extracting("name")
                        .containsExactlyInAnyOrder("author_name_1", "author_name_2"),
                () -> assertThat(savedBook.getBookTranslators().stream().map(BookTranslator::getTranslator))
                        .hasSize(2)
                        .extracting("name")
                        .containsExactlyInAnyOrder("translator_name_1", "translator_name_2")
        );
    }

    private List<BookTranslator> createTranslator() {
        return List.of(BookTranslator.builder()
                .translator(Translator.builder()
                        .name("translator_name_1")
                        .build())
                .build(), BookTranslator.builder()
                .translator(Translator.builder()
                        .name("translator_name_2")
                        .build())
                .build());
    }

    private List<BookAuthor> createAuthor() {
        return List.of(BookAuthor.builder()
                .author(Author.builder()
                        .name("author_name_1")
                        .build())
                .build(), BookAuthor.builder()
                .author(Author.builder()
                        .name("author_name_2")
                        .build())
                .build());
    }

    private Book createBook() {
        return Book.builder()
                .isbn10("isbn10_1")
                .isbn13("isbn13_1")
                .title("title_1")
                .description("description_1")
                .publisher("publisher_1")
                .publishDate(LocalDateTime.now())
                .price(10000)
                .thumbnailUrl("thumbnailUrl_1")
                .build();
    }
}