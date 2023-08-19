package kr.mybrary.bookservice.mybook.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import kr.mybrary.bookservice.PersistenceTest;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.bookInfo.Author;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookAuthor;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import kr.mybrary.bookservice.mybook.MyBookFixture;
import kr.mybrary.bookservice.mybook.persistence.model.MyBookListDisplayElementModel;
import kr.mybrary.bookservice.mybook.persistence.repository.MyBookRepository;
import kr.mybrary.bookservice.tag.MeaningTagFixture;
import kr.mybrary.bookservice.tag.MyBookMeaningTagFixture;
import kr.mybrary.bookservice.tag.persistence.MeaningTag;
import kr.mybrary.bookservice.tag.persistence.MyBookMeaningTag;
import kr.mybrary.bookservice.tag.persistence.repository.MeaningTagRepository;
import kr.mybrary.bookservice.tag.persistence.repository.MyBookMeaningTagRepository;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@PersistenceTest
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
        Book savedBook = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBook());
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
        Book savedBook = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBook());
        MyBook myBook = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookWithBook(savedBook);

        MyBook savedMyBook = myBookRepository.save(myBook);

        entityManager.flush();
        entityManager.clear();

        // when, then
        assertThat(myBookRepository.existsByUserIdAndBook(savedMyBook.getUserId(),
                savedMyBook.getBook())).isTrue();
    }

    @DisplayName("마이북을 모두 조회한다. (삭제된 책은 보여주지 않는다.)")
    @Test
    void findAllMyBooks() {

        // given
        Book savedBook_1 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().isbn10("isbn10_1").isbn13("isbn13_1").build());
        Book savedBook_2 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().isbn10("isbn10_2").isbn13("isbn13_2").build());
        Book savedBook_3 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().isbn10("isbn10_3").isbn13("isbn13_3").build());

        MyBook myBook_1 = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookBuilder()
                .id(1L).book(savedBook_1).build();
        MyBook myBook_2 = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookBuilder()
                .id(2L).book(savedBook_2).build();
        MyBook myBook_3 = MyBookFixture.DELETED_LOGIN_USER_MYBOOK.getMyBookBuilder()
                .id(3L).book(savedBook_3).build();

        myBookRepository.save(myBook_1);
        myBookRepository.save(myBook_2);
        myBookRepository.save(myBook_3);

        entityManager.flush();
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
        Book savedBook_1 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().isbn10("isbn10_1").isbn13("isbn13_1").build());
        Book savedBook_2 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().isbn10("isbn10_2").isbn13("isbn13_2").build());

        MyBook myBook_1 = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookBuilder()
                .id(1L).book(savedBook_1).build();
        MyBook myBook_2 = MyBookFixture.DELETED_LOGIN_USER_MYBOOK.getMyBookBuilder()
                .id(2L).book(savedBook_2).build();

        MyBook savedMyBook = myBookRepository.save(myBook_1);
        MyBook savedDeletedMyBook = myBookRepository.save(myBook_2);

        entityManager.flush();
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

        Book book_1 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().id(1L).isbn10("isbn10_1").isbn13("isbn13_1").build());
        Book book_2 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().id(2L).isbn10("isbn10_2").isbn13("isbn13_2").build());
        Book book_3 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().id(3L).isbn10("isbn10_3").isbn13("isbn13_3").build());
        Book book_4 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().id(4L).isbn10("isbn10_4").isbn13("isbn13_4").build());

        MyBook myBook_1 = myBookRepository.save(
                MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookBuilder().id(1L).book(book_1).build());
        MyBook myBook_2 = myBookRepository.save(
                MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookBuilder().id(2L).book(book_2).build());
        MyBook myBook_3 = myBookRepository.save(
                MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookBuilder().id(3L).book(book_3).build());
        myBookRepository.save(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookBuilder().id(4L).book(book_4).build());

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

        entityManager.flush();
        entityManager.clear();

        // when
        List<MyBook> myBooks = myBookRepository.findByMeaningTagQuote("meaningTag_2");

        // then
        assertAll(
                () -> assertThat(myBooks.size()).isEqualTo(2)
        );
    }

    @DisplayName("마이북 상세 조회 시, fetch join을 통해 연관된 의미 태그를 함께 조회한다.")
    @Test
    void findMyBookDetailUsingFetchJoin() {

        // given
        MeaningTag meaningTag = meaningTagRepository.save(MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTagBuilder()
                .id(1L).quote("meaningTag").build());

        Book book = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBook());

        MyBook myBook = myBookRepository.save(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookBuilder()
                .id(1L).book(book).build());

        MyBookMeaningTag myBookMeaningTag = MyBookMeaningTagFixture.MY_BOOK_MEANING_TAG_WITHOUT_RELATION.getMyBookMeaningTagBuilder()
                .id(1L)
                .meaningTag(meaningTag)
                .myBook(myBook).build();

        myBookMeaningTagRepository.save(myBookMeaningTag);

        entityManager.flush();
        entityManager.clear();

        // then
        Optional<MyBook> myBookDetail = myBookRepository.findMyBookDetailUsingFetchJoin(myBook.getId());

        // when
        assertAll(
                () -> {
                    assertThat(myBookDetail.isPresent()).isTrue();
                    assertThat(myBookDetail.get().getBook() instanceof HibernateProxy).isFalse();
                    assertThat(myBookDetail.get().getMyBookMeaningTag() instanceof HibernateProxy).isFalse();
                    assertThat(myBookDetail.get().getMyBookMeaningTag().getMeaningTag() instanceof HibernateProxy).isFalse();
                    assertThat(myBookDetail.get().getBook().getTitle()).isEqualTo(book.getTitle());
                    assertThat(myBookDetail.get().getBook().getIsbn13()).isEqualTo(book.getIsbn13());
                    assertThat(myBookDetail.get().getMyBookMeaningTag().getMeaningTagColor()).isEqualTo(myBookMeaningTag.getMeaningTagColor());
                    assertThat(myBookDetail.get().getMyBookMeaningTag().getMeaningTag().getQuote()).isEqualTo(meaningTag.getQuote());
                }
        );
    }

    @DisplayName("fetch join을 통해 의미 태그가 적용되지 않은 마이북 상세 조회한다.")
    @Test
    void findMyBookDetailWithOutMeaningTag() {

        // given
        Book book = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBook());

        MyBook myBook = myBookRepository.saveAndFlush(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookBuilder()
                .id(1L).book(book).build());

        entityManager.flush();
        entityManager.clear();

        // then
        Optional<MyBook> myBookDetail = myBookRepository.findMyBookDetailUsingFetchJoin(myBook.getId());

        // when
        assertAll(
                () -> {
                    assertThat(myBookDetail.isPresent()).isTrue();
                    assertThat(myBookDetail.get().getBook() instanceof HibernateProxy).isFalse();
                    assertThat(myBookDetail.get().getBook().getTitle()).isEqualTo(book.getTitle());
                    assertThat(myBookDetail.get().getBook().getIsbn13()).isEqualTo(book.getIsbn13());
                    assertThat(myBookDetail.get().getMyBookMeaningTag()).isNull();
                }
        );
    }

    @DisplayName("fetch join을 통해 마이북 조회시, 도서도 함께 조회한다.")
    @Test
    void findMyBookWithBook() {

        // given
        Book book = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBook());
        MyBook myBook = entityManager.persist(
                MyBookFixture.MY_BOOK_WITHOUT_RELATION.getMyBookBuilder().book(book).build());

        entityManager.flush();
        entityManager.clear();

        // when
        Optional<MyBook> foundMyBook = myBookRepository.findByIdWithBook(myBook.getId());

        // then
        assertAll(
                () -> {
                    assertThat(foundMyBook.isPresent()).isTrue();
                    assertThat(foundMyBook.get().getBook() instanceof HibernateProxy).isFalse();
                    assertThat(foundMyBook.get().getBook().getIsbn13()).isEqualTo(book.getIsbn13());
                }
        );
    }

    @DisplayName("한 유저의 모든 마이북 리스트를 조회한다.")
    @Test
    void getAllMyBookList() {

        // given
        Author author_1 = entityManager.persist(Author.builder().aid(11).name("테스트 저자 1").build());
        Author author_2 = entityManager.persist(Author.builder().aid(12).name("테스트 저자 2").build());
        Author author_3 = entityManager.persist(Author.builder().aid(13).name("테스트 저자 3").build());

        Book savedBook_1 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().isbn10("isbn10_1").isbn13("isbn13_1").build());
        Book savedBook_2 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().isbn10("isbn10_2").isbn13("isbn13_2").build());
        Book savedBook_3 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().isbn10("isbn10_3").isbn13("isbn13_3").build());

        entityManager.persist(BookAuthor.builder().book(savedBook_1).author(author_1).build());
        entityManager.persist(BookAuthor.builder().book(savedBook_1).author(author_2).build());
        entityManager.persist(BookAuthor.builder().book(savedBook_2).author(author_1).build());
        entityManager.persist(BookAuthor.builder().book(savedBook_3).author(author_3).build());

        MyBook myBook_1 = entityManager.persist(MyBookFixture.MY_BOOK_WITHOUT_RELATION.getMyBookBuilder()
                .book(savedBook_1).showable(true).readStatus(ReadStatus.COMPLETED).build());
        MyBook myBook_2 = entityManager.persist(MyBookFixture.MY_BOOK_WITHOUT_RELATION.getMyBookBuilder()
                .book(savedBook_2).showable(true).readStatus(ReadStatus.COMPLETED).build());
        MyBook myBook_3 = entityManager.persist(MyBookFixture.MY_BOOK_WITHOUT_RELATION.getMyBookBuilder()
                .book(savedBook_3).showable(true).readStatus(ReadStatus.TO_READ).build());

        entityManager.flush();
        entityManager.clear();

        // when
        List<MyBookListDisplayElementModel> myBookList = myBookRepository.findMyBookListDisplayElementModelsByUserId(
                "LOGIN_USER_ID", MyBookOrderType.NONE, null);

        // then
        assertAll(
                () -> assertThat(myBookList.size()).isEqualTo(3),
                () -> assertThat(myBookList).extracting("myBookId")
                        .contains(myBook_1.getId(), myBook_2.getId(), myBook_3.getId()),
                () -> assertThat(myBookList.get(0).getBookAuthors().get(0).getAuthor()).isNotInstanceOf(HibernateProxy.class),
                () -> assertThat(myBookList.get(1).getBookAuthors().get(0).getAuthor()).isNotInstanceOf(HibernateProxy.class),
                () -> assertThat(myBookList.get(2).getBookAuthors().get(0).getAuthor()).isNotInstanceOf(HibernateProxy.class)
        );
    }

    @DisplayName("한 유저의 완독한 마이북 리스트를 조회한다.")
    @Test
    void getMyBookListReadDone() {

        // given
        Author author_1 = entityManager.persist(Author.builder().aid(11).name("테스트 저자 1").build());
        Author author_2 = entityManager.persist(Author.builder().aid(12).name("테스트 저자 2").build());
        Author author_3 = entityManager.persist(Author.builder().aid(13).name("테스트 저자 3").build());

        Book savedBook_1 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().isbn10("isbn10_1").isbn13("isbn13_1").build());
        Book savedBook_2 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().isbn10("isbn10_2").isbn13("isbn13_2").build());
        Book savedBook_3 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().isbn10("isbn10_3").isbn13("isbn13_3").build());

        entityManager.persist(BookAuthor.builder().book(savedBook_1).author(author_1).build());
        entityManager.persist(BookAuthor.builder().book(savedBook_1).author(author_2).build());
        entityManager.persist(BookAuthor.builder().book(savedBook_2).author(author_1).build());
        entityManager.persist(BookAuthor.builder().book(savedBook_3).author(author_3).build());

        MyBook myBook_1 = entityManager.persist(MyBookFixture.MY_BOOK_WITHOUT_RELATION.getMyBookBuilder()
                .book(savedBook_1).shareable(true).readStatus(ReadStatus.COMPLETED).build());
        MyBook myBook_2 = entityManager.persist(MyBookFixture.MY_BOOK_WITHOUT_RELATION.getMyBookBuilder()
                .book(savedBook_2).shareable(true).readStatus(ReadStatus.COMPLETED).build());
        entityManager.persist(MyBookFixture.MY_BOOK_WITHOUT_RELATION.getMyBookBuilder().book(savedBook_3).shareable(true).readStatus(ReadStatus.TO_READ).build());

        entityManager.flush();
        entityManager.clear();

        // when
        List<MyBookListDisplayElementModel> myBookList = myBookRepository.findMyBookListDisplayElementModelsByUserId(
                "LOGIN_USER_ID", MyBookOrderType.NONE, ReadStatus.COMPLETED);

        // then
        assertAll(
                () -> assertThat(myBookList.size()).isEqualTo(2),
                () -> assertThat(myBookList).extracting("myBookId")
                        .contains(myBook_1.getId(), myBook_2.getId())
        );
    }

    @Test
    @DisplayName("오늘 등록된 마이북의 갯수를 조회한다.")
    void getTodayBookRegistrationCount() {

        // given
        LocalDate today = LocalDate.now();

        Book savedBook_1 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().isbn10("isbn10_1").isbn13("isbn13_1").build());
        Book savedBook_2 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().isbn10("isbn10_2").isbn13("isbn13_2").build());
        Book savedBook_3 = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().isbn10("isbn10_3").isbn13("isbn13_3").build());

        entityManager.persist(MyBookFixture.MY_BOOK_WITHOUT_RELATION.getMyBookBuilder()
                .book(savedBook_1).build());
        entityManager.persist(MyBookFixture.MY_BOOK_WITHOUT_RELATION.getMyBookBuilder()
                .book(savedBook_2).build());
        entityManager.persist(MyBookFixture.MY_BOOK_WITHOUT_RELATION.getMyBookBuilder()
                .book(savedBook_3).build());

        entityManager.flush();
        entityManager.clear();

        // when
        Long todayBookRegistrationCount = myBookRepository.getBookRegistrationCountOfDay(today);
        Long yesterdayBookRegistrationCount = myBookRepository.getBookRegistrationCountOfDay(today.minusDays(1));

        // then
        assertAll(
                () -> assertThat(todayBookRegistrationCount).isEqualTo(3L),
                () -> assertThat(yesterdayBookRegistrationCount).isEqualTo(0L)
        );
    }

    @DisplayName("유저 ID와 Book 엔티티를 통해 마이북을 조회한다.")
    @Test
    void findByUserIdAndBook() {

        // given
        Book savedBook = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBook());
        MyBook myBook = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookWithBook(savedBook);
        String userId = myBook.getUserId();

        MyBook savedMyBook = myBookRepository.save(myBook);

        entityManager.flush();
        entityManager.clear();

        // when
        Optional<MyBook> foundMyBook = myBookRepository.findByUserIdAndBook(userId, savedBook);

        // then
        assertAll(
                () -> {
                    assertThat(foundMyBook.isPresent()).isTrue();
                    assertThat(foundMyBook.get().getId()).isEqualTo(savedMyBook.getId());
                    assertThat(foundMyBook.get().getUserId()).isEqualTo(savedMyBook.getUserId());
                }
        );
    }
}