package kr.mybrary.bookservice.mybook.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import kr.mybrary.bookservice.mybook.MyBookFixture;
import kr.mybrary.bookservice.mybook.persistence.repository.MyBookRepository;
import kr.mybrary.bookservice.tag.MeaningTagFixture;
import kr.mybrary.bookservice.tag.MyBookMeaningTagFixture;
import kr.mybrary.bookservice.tag.persistence.MeaningTag;
import kr.mybrary.bookservice.tag.persistence.repository.MeaningTagRepository;
import kr.mybrary.bookservice.tag.persistence.repository.MyBookMeaningTagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MyBookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MyBookRepository myBookRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    MeaningTagRepository meaningTagRepository;

    @Autowired
    MyBookMeaningTagRepository myBookMeaningTagRepository;

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
                () -> assertThat(savedMyBook.getBook().getIsbn10()).isEqualTo(
                        myBook.getBook().getIsbn10()),
                () -> assertThat(savedMyBook.getBook().getIsbn13()).isEqualTo(
                        myBook.getBook().getIsbn13()),
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

        entityManager.clear();

        // when, then
        assertThat(myBookRepository.existsByUserIdAndBook(savedMyBook.getUserId(),
                savedMyBook.getBook())).isTrue();
    }

    @DisplayName("마이북을 모두 조회한다. (삭제된 책은 보여주지 않는다.)")
    @Test
    void findAllMyBooks() {

        // given
        Book savedBook_1 = bookRepository.save(BookFixture.COMMON_BOOK.getBook());
        Book savedBook_2 = bookRepository.save(BookFixture.COMMON_BOOK.getBook());
        Book savedBook_3 = bookRepository.save(BookFixture.COMMON_BOOK.getBook());

        MyBook myBook_1 = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookBuilder()
                .id(1L).book(savedBook_1).build();
        MyBook myBook_2 = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookBuilder()
                .id(2L).book(savedBook_2).build();
        MyBook myBook_3 = MyBookFixture.DELETED_LOGIN_USER_MYBOOK.getMyBookBuilder()
                .id(3L).book(savedBook_3).build();

        myBookRepository.save(myBook_1);
        myBookRepository.save(myBook_2);
        myBookRepository.save(myBook_3);

        entityManager.clear();

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

        MyBook myBook_1 = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookBuilder()
                .id(1L).book(savedBook_1).build();
        MyBook myBook_2 = MyBookFixture.DELETED_LOGIN_USER_MYBOOK.getMyBookBuilder()
                .id(2L).book(savedBook_2).build();

        MyBook savedMyBook = myBookRepository.save(myBook_1);
        MyBook savedDeletedMyBook = myBookRepository.save(myBook_2);

        entityManager.clear();

        // when, then
        assertAll(
                () -> assertThat(myBookRepository.findById(savedMyBook.getId()).isPresent()).isTrue(),
                () -> assertThat(myBookRepository.findById(savedDeletedMyBook.getId()).isEmpty()).isTrue()
        );

    }

    @DisplayName("의미 태그 문구를 통해서 모든 마이북을 조회한다.")
    @Test
    void findAllMyBooksByMeaningTag() {

        // given
        MeaningTag meaningTag_1 = meaningTagRepository.save(MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTagBuilder()
                .id(1L).quote("meaningTag_1").build());

        MeaningTag meaningTag_2 = meaningTagRepository.save(MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTagBuilder()
                .id(2L).quote("meaningTag_2").build());

        Book book_1 = bookRepository.save(BookFixture.COMMON_BOOK.getBook());
        Book book_2 = bookRepository.save(BookFixture.COMMON_BOOK.getBook());

        MyBook myBook_1 = myBookRepository.save(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookBuilder().id(1L).book(book_1).build());
        MyBook myBook_2 = myBookRepository.save(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookBuilder().id(2L).book(book_2).build());
        MyBook myBook_3 = myBookRepository.save(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookBuilder().id(3L).book(book_2).build());

        myBookMeaningTagRepository.save(MyBookMeaningTagFixture.COMMON_MY_BOOK_MEANING_TAG.getMyBookMeaningTagBuilder()
                        .id(1L)
                        .meaningTag(meaningTag_1)
                        .myBook(myBook_1).build());

        myBookMeaningTagRepository.save(MyBookMeaningTagFixture.COMMON_MY_BOOK_MEANING_TAG.getMyBookMeaningTagBuilder()
                        .id(2L)
                        .meaningTag(meaningTag_2)
                        .myBook(myBook_2).build());

        myBookMeaningTagRepository.save(MyBookMeaningTagFixture.COMMON_MY_BOOK_MEANING_TAG.getMyBookMeaningTagBuilder()
                        .id(3L)
                        .meaningTag(meaningTag_2)
                        .myBook(myBook_3).build());

        entityManager.clear();

        // when
        List<MyBook> myBooks = myBookRepository.findByMeaningTagQuote("meaningTag_2");

        // then
        assertAll(
                () -> assertThat(myBooks.size()).isEqualTo(2)
        );
    }
}