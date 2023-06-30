package kr.mybrary.bookservice.mybook.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import kr.mybrary.bookservice.mybook.persistence.repository.MyBookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MyBookRepositoryTest {

    @Autowired
    MyBookRepository myBookRepository;

    @Autowired
    BookRepository bookRepository;

    @DisplayName("마이북을 저장한다.")
    @Test
    void saveAuthor() {
        // given
        Book savedBook = bookRepository.save(createBook());
        MyBook myBook = createMyBook(savedBook);

        // when
        MyBook savedMyBook = myBookRepository.save(myBook);

        // then
        assertAll(
                () -> assertThat(savedMyBook.getUserId()).isEqualTo(myBook.getUserId()),
                () -> assertThat(savedMyBook.getBook().getIsbn10()).isEqualTo("isbn10"),
                () -> assertThat(savedMyBook.getBook().getIsbn13()).isEqualTo("isbn13"),
                () -> assertThat(savedMyBook.isPublic()).isEqualTo(false),
                () -> assertThat(savedMyBook.isDeleted()).isEqualTo(false),
                () -> assertThat(savedMyBook.isExchangeable()).isEqualTo(false),
                () -> assertThat(savedMyBook.isShareable()).isEqualTo(false)
        );
    }

    @DisplayName("이미 등록한 마이북인지 확인한다.")
    @Test
    void existsByUserIdAndBook() {
        // given
        Book savedBook = bookRepository.save(createBook());
        MyBook myBook = createMyBook(savedBook);

        MyBook savedMyBook = myBookRepository.save(myBook);

        // when, then
        assertThat(myBookRepository.existsByUserIdAndBook(savedMyBook.getUserId(), savedMyBook.getBook())).isTrue();
    }

    private MyBook createMyBook(Book book) {
        return MyBook.builder()
                .userId("user_id")
                .book(book)
                .isDeleted(false)
                .isExchangeable(false)
                .isShareable(false)
                .isPublic(false)
                .readStatus(ReadStatus.READING)
                .startDateOfPossession(LocalDateTime.now())
                .build();
    }

    private Book createBook() {
        return Book.builder()
                .isbn10("isbn10")
                .isbn13("isbn13")
                .title("title")
                .description("description")
                .publisher("publisher")
                .publishDate(LocalDateTime.now())
                .price(10000)
                .thumbnailUrl("thumbnailUrl")
                .build();
    }
}