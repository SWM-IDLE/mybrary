package kr.mybrary.bookservice.book.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.mybrary.bookservice.PersistenceTest;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.BookInterestFixture;
import kr.mybrary.bookservice.book.persistence.repository.BookInterestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@PersistenceTest
class BookInterestRepositoryTest {

    @Autowired
    BookInterestRepository bookInterestRepository;

    @Autowired
    TestEntityManager entityManager;

    @DisplayName("도서와 사용자 아이디로 관심 도서를 조회한다.")
    @Test
    void findByBookAndUserId() {

        // given
        Book book = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().build());

        BookInterest bookInterest = BookInterestFixture.COMMON_BOOK_INTEREST.getBookInterestBuilder()
                .id(null)
                .book(book)
                .build();

        entityManager.persist(bookInterest);

        entityManager.flush();
        entityManager.clear();

        // when
        Optional<BookInterest> foundBookInterest = bookInterestRepository.findByBookAndUserId(bookInterest.getBook(),
                bookInterest.getUserId());

        // then
        assertAll(
                () -> {
                    assertThat(foundBookInterest.isPresent()).isTrue();
                    assertThat(foundBookInterest.get().getBook().getIsbn13()).isEqualTo(book.getIsbn13());
                    assertThat(foundBookInterest.get().getUserId()).isEqualTo(bookInterest.getUserId());
                }
        );
    }

    @DisplayName("사용자 ID를 통해 관심 도서를 조회한다. - 정렬 조건 없음")
    @Test
    void findInterestBookByUserIdAndOrderByNone() {

        // given
        String loginId = "LOGIN_USER_ID";
        Book book_1 = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().title("title1")
                .isbn10("isbn10_1").isbn13("isbn13_1")
                .publicationDate(LocalDateTime.of(2023, 1, 1, 0, 0)).build());

        Book book_2 = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().title("title2")
                .isbn10("isbn10_2").isbn13("isbn13_2")
                .publicationDate(LocalDateTime.of(2023, 3, 1, 0, 0)).build());

        Book book_3 = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().title("title3")
                .isbn10("isbn10_3").isbn13("isbn13_3")
                .publicationDate(LocalDateTime.of(2023, 2, 1, 0, 0)).build());

        BookInterest bookInterest_1 = entityManager.persist(BookInterestFixture.BOOK_INTEREST_WITHOUT_RELATION
                .getBookInterestBuilder().book(book_1).userId(loginId).build());

        BookInterest bookInterest_2 = entityManager.persist(BookInterestFixture.BOOK_INTEREST_WITHOUT_RELATION
                .getBookInterestBuilder().book(book_2).userId(loginId).build());

        BookInterest bookInterest_3 = entityManager.persist(BookInterestFixture.BOOK_INTEREST_WITHOUT_RELATION
                .getBookInterestBuilder().book(book_3).userId(loginId).build());

        entityManager.flush();
        entityManager.clear();

        // when
        List<BookInterest> SortByNone = bookInterestRepository.findAllByUserIdWithBook(loginId, BookOrderType.NONE);

        // given
        assertAll(
                () -> assertThat(SortByNone.size()).isEqualTo(3),
                () -> assertThat(SortByNone).extracting("id")
                        .containsExactly(bookInterest_1.getId(), bookInterest_2.getId(), bookInterest_3.getId())
        );
    }
    @DisplayName("사용자 ID를 통해 관심 도서를 조회한다. - 초성순 정렬")
    @Test
    void findInterestBookByUserIdAndOrderByInitial() {

        // given
        String loginId = "LOGIN_USER_ID";
        Book book_1 = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().title("title1")
                .isbn10("isbn10_1").isbn13("isbn13_1")
                .publicationDate(LocalDateTime.of(2023, 1, 1, 0, 0)).build());

        Book book_2 = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().title("title2")
                .isbn10("isbn10_2").isbn13("isbn13_2")
                .publicationDate(LocalDateTime.of(2023, 3, 1, 0, 0)).build());

        Book book_3 = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().title("title3")
                .isbn10("isbn10_3").isbn13("isbn13_3")
                .publicationDate(LocalDateTime.of(2023, 2, 1, 0, 0)).build());

        BookInterest bookInterest_1 = entityManager.persist(BookInterestFixture.BOOK_INTEREST_WITHOUT_RELATION
                .getBookInterestBuilder().book(book_1).userId(loginId).build());

        BookInterest bookInterest_2 = entityManager.persist(BookInterestFixture.BOOK_INTEREST_WITHOUT_RELATION
                .getBookInterestBuilder().book(book_2).userId(loginId).build());

        BookInterest bookInterest_3 = entityManager.persist(BookInterestFixture.BOOK_INTEREST_WITHOUT_RELATION
                .getBookInterestBuilder().book(book_3).userId(loginId).build());

        entityManager.flush();
        entityManager.clear();

        // when
        List<BookInterest> sortByInitial = bookInterestRepository.findAllByUserIdWithBook(loginId, BookOrderType.TITLE);

        // given
        assertAll(
                () -> assertThat(sortByInitial.size()).isEqualTo(3),
                () -> assertThat(sortByInitial).extracting("id")
                        .containsExactly(bookInterest_1.getId(), bookInterest_2.getId(), bookInterest_3.getId())
        );
    }
    @DisplayName("사용자 ID를 통해 관심 도서를 조회한다. - 발행일순 정렬")
    @Test
    void findInterestBookByUserIdAndOrderByPublication() {

        // given
        String loginId = "LOGIN_USER_ID";
        Book book_1 = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().title("title1")
                .isbn10("isbn10_1").isbn13("isbn13_1")
                .publicationDate(LocalDateTime.of(2023, 1, 1, 0, 0)).build());

        Book book_2 = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().title("title2")
                .isbn10("isbn10_2").isbn13("isbn13_2")
                .publicationDate(LocalDateTime.of(2023, 3, 1, 0, 0)).build());

        Book book_3 = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().title("title3")
                .isbn10("isbn10_3").isbn13("isbn13_3")
                .publicationDate(LocalDateTime.of(2023, 2, 1, 0, 0)).build());

        BookInterest bookInterest_1 = entityManager.persist(BookInterestFixture.BOOK_INTEREST_WITHOUT_RELATION
                .getBookInterestBuilder().book(book_1).userId(loginId).build());

        BookInterest bookInterest_2 = entityManager.persist(BookInterestFixture.BOOK_INTEREST_WITHOUT_RELATION
                .getBookInterestBuilder().book(book_2).userId(loginId).build());

        BookInterest bookInterest_3 = entityManager.persist(BookInterestFixture.BOOK_INTEREST_WITHOUT_RELATION
                .getBookInterestBuilder().book(book_3).userId(loginId).build());

        entityManager.flush();
        entityManager.clear();

        // when
        List<BookInterest> sortByPublication = bookInterestRepository.findAllByUserIdWithBook(loginId, BookOrderType.PUBLICATION);

        // given
        assertAll(
                () -> assertThat(sortByPublication.size()).isEqualTo(3),
                () -> assertThat(sortByPublication).extracting("id")
                        .containsExactly(bookInterest_2.getId(), bookInterest_3.getId(), bookInterest_1.getId())
        );
    }
    @DisplayName("사용자 ID를 통해 관심 도서를 조회한다. - 등록순 정렬")
    @Test
    void findInterestBookByUserIdAndOrderByRegistration() {

        // given
        String loginId = "LOGIN_USER_ID";
        Book book_1 = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().title("title1")
                .isbn10("isbn10_1").isbn13("isbn13_1")
                .publicationDate(LocalDateTime.of(2023, 1, 1, 0, 0)).build());

        Book book_2 = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().title("title2")
                .isbn10("isbn10_2").isbn13("isbn13_2")
                .publicationDate(LocalDateTime.of(2023, 3, 1, 0, 0)).build());

        Book book_3 = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().title("title3")
                .isbn10("isbn10_3").isbn13("isbn13_3")
                .publicationDate(LocalDateTime.of(2023, 2, 1, 0, 0)).build());

        BookInterest bookInterest_1 = entityManager.persist(BookInterestFixture.BOOK_INTEREST_WITHOUT_RELATION
                .getBookInterestBuilder().book(book_1).userId(loginId).build());

        BookInterest bookInterest_2 = entityManager.persist(BookInterestFixture.BOOK_INTEREST_WITHOUT_RELATION
                .getBookInterestBuilder().book(book_2).userId(loginId).build());

        BookInterest bookInterest_3 = entityManager.persist(BookInterestFixture.BOOK_INTEREST_WITHOUT_RELATION
                .getBookInterestBuilder().book(book_3).userId(loginId).build());

        entityManager.flush();
        entityManager.clear();

        // when
        List<BookInterest> SortByRegistration = bookInterestRepository.findAllByUserIdWithBook(loginId, BookOrderType.REGISTRATION);

        // given
        assertAll(
                () -> assertThat(SortByRegistration.size()).isEqualTo(3),
                () -> assertThat(SortByRegistration).extracting("id")
                        .containsExactly(bookInterest_3.getId(), bookInterest_2.getId(), bookInterest_1.getId())
        );
    }

    @DisplayName("도서와 로그인 유저 ID로 해당 도서의 관심 도서 여부를 조회한다.")
    @Test
    void existsByBookAndUserId() {

        // given
        Book book = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBookBuilder().build());

        BookInterest bookInterest = BookInterestFixture.BOOK_INTEREST_WITHOUT_RELATION.getBookInterestBuilder()
                .book(book)
                .build();

        entityManager.persist(bookInterest);

        entityManager.flush();
        entityManager.clear();

        // when
        boolean result = bookInterestRepository.existsByBookAndUserId(book, "LOGIN_USER_ID");

        // then
        assertAll(
                () -> assertThat(result).isTrue()
        );
    }
}