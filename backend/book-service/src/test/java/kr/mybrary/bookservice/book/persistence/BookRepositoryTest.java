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
        book.addBookAuthorAndBookTranslator(createAuthor(), createTranslator());

        // when
        Book savedBook = bookRepository.save(book);

        // then
        assertAll(
                () -> assertThat(savedBook.getTitle()).isEqualTo(book.getTitle()),
                () -> assertThat(savedBook.getIsbn10()).isEqualTo(book.getIsbn10()),
                () -> assertThat(savedBook.getIsbn13()).isEqualTo(book.getIsbn13()),
                () -> assertThat(savedBook.getBookTranslators().size()).isEqualTo(2),
                () -> assertThat(savedBook.getBookAuthors().size()).isEqualTo(2)
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
    private Book createAnotherBook() {
        return Book.builder()
                .isbn10("isbn10_2")
                .isbn13("isbn13_2")
                .title("title_2")
                .description("description_2")
                .publisher("publisher_2")
                .publishDate(LocalDateTime.now())
                .price(20000)
                .thumbnailUrl("thumbnailUrl_2")
                .build();
    }
}