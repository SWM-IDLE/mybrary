package kr.mybrary.bookservice.mybook.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import kr.mybrary.bookservice.mybook.MyBookFixture;
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
    void saveMybook() {
        // given
        Book savedBook = bookRepository.save(BookFixture.COMMON_BOOK.getBook());
        MyBook myBook = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookWithBook(savedBook);

        // when
        MyBook savedMyBook = myBookRepository.save(myBook);

        // then
        assertAll(
                () -> assertThat(savedMyBook.getUserId()).isEqualTo(myBook.getUserId()),
                () -> assertThat(savedMyBook.getBook().getIsbn10()).isEqualTo(myBook.getBook().getIsbn10()),
                () -> assertThat(savedMyBook.getBook().getIsbn13()).isEqualTo(myBook.getBook().getIsbn13()),
                () -> assertThat(savedMyBook.getReadStatus()).isEqualTo(ReadStatus.TO_READ),
                () -> assertThat(savedMyBook.isShowable()).isEqualTo(true),
                () -> assertThat(savedMyBook.isDeleted()).isEqualTo(false),
                () -> assertThat(savedMyBook.isExchangeable()).isEqualTo(false),
                () -> assertThat(savedMyBook.isShareable()).isEqualTo(false)
        );
    }

    @DisplayName("이미 등록한 마이북인지 확인한다.")
    @Test
    void existsByUserIdAndBook() {
        // given
        Book savedBook = bookRepository.save(BookFixture.COMMON_BOOK.getBook());
        MyBook myBook = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookWithBook(savedBook);

        MyBook savedMyBook = myBookRepository.save(myBook);

        // when, then
        assertThat(myBookRepository.existsByUserIdAndBook(savedMyBook.getUserId(),
                savedMyBook.getBook())).isTrue();
    }

    @DisplayName("마이북을 모두 조회한다.")
    @Test
    void findAllMyBooks() {

        // given
        Book savedBook_1 = bookRepository.save(BookFixture.COMMON_BOOK.getBook());
        Book savedBook_2 = bookRepository.save(BookFixture.COMMON_BOOK.getBook());

        MyBook myBook_1 = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookWithBook(savedBook_1);
        MyBook myBook_2 = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookWithBook(savedBook_2);

        myBookRepository.save(myBook_1);
        myBookRepository.save(myBook_2);

        // when
        List<MyBook> myBooks = myBookRepository.findAllByUserId("LOGIN_USER_ID");

        // then
        assertThat(myBooks.size()).isEqualTo(2);
    }

    @DisplayName("마이북 ID로 마이북을 조회한다. (삭제된 책은 보여주지 않는다.)")
    @Test
    void findMyBookById() {

        // given
        Book savedBook_1 = bookRepository.save(BookFixture.COMMON_BOOK.getBook());
        Book savedBook_2 = bookRepository.save(BookFixture.COMMON_BOOK.getBook());

        MyBook myBook_1 = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookWithBook(savedBook_1);
        MyBook myBook_2 = MyBookFixture.DELETED_LOGIN_USER_MYBOOK.getMyBookWithBook(savedBook_2);

        MyBook savedMyBook = myBookRepository.save(myBook_1);
        MyBook savedDeletedMyBook = myBookRepository.save(myBook_2);

        // when
        assertAll(
                () -> assertThat(myBookRepository.findByIdAndDeletedIsFalse(savedMyBook.getId())
                        .isPresent()).isTrue(),
                () -> assertThatThrownBy(() -> myBookRepository.findByIdAndDeletedIsFalse(savedDeletedMyBook.getId())
                                .orElseThrow(IllegalArgumentException::new))
        );

    }
}