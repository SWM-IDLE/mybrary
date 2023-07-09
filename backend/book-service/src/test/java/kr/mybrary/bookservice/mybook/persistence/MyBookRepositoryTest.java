package kr.mybrary.bookservice.mybook.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kr.mybrary.bookservice.book.BookTestData;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import kr.mybrary.bookservice.mybook.MybookTestData;
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
        Book savedBook = bookRepository.save(BookTestData.createBook());
        MyBook myBook = MybookTestData.createMyBook(savedBook);

        // when
        MyBook savedMyBook = myBookRepository.save(myBook);

        // then
        assertAll(
                () -> assertThat(savedMyBook.getUserId()).isEqualTo(myBook.getUserId()),
                () -> assertThat(savedMyBook.getBook().getIsbn10()).isEqualTo(myBook.getBook().getIsbn10()),
                () -> assertThat(savedMyBook.getBook().getIsbn13()).isEqualTo(myBook.getBook().getIsbn13()),
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
        Book savedBook = bookRepository.save(BookTestData.createBook());
        MyBook myBook = MybookTestData.createMyBook(savedBook);

        MyBook savedMyBook = myBookRepository.save(myBook);

        // when, then
        assertThat(myBookRepository.existsByUserIdAndBook(savedMyBook.getUserId(),
                savedMyBook.getBook())).isTrue();
    }

    @DisplayName("마이북을 모두 조회한다.")
    @Test
    void findAllMyBooks() {

        // given
        Book savedBook_1 = bookRepository.save(BookTestData.createBook());
        Book savedBook_2 = bookRepository.save(BookTestData.createBook());

        MyBook myBook_1 = MybookTestData.createMyBook(savedBook_1);
        MyBook myBook_2 = MybookTestData.createMyBook(savedBook_2);

        myBookRepository.save(myBook_1);
        myBookRepository.save(myBook_2);

        // when
        List<MyBook> myBooks = myBookRepository.findAllByUserId("test_userId");

        // then
        assertThat(myBooks.size()).isEqualTo(2);
    }
}