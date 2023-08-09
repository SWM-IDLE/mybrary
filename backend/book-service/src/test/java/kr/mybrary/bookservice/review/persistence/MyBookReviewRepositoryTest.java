package kr.mybrary.bookservice.review.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import kr.mybrary.bookservice.PersistenceTest;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.MyBookFixture;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.MyBookReviewFixture;
import kr.mybrary.bookservice.review.persistence.model.MyBookReviewElementDto;
import kr.mybrary.bookservice.review.persistence.model.ReviewFromMyBookModel;
import kr.mybrary.bookservice.review.persistence.repository.MyBookReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@PersistenceTest
class MyBookReviewRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MyBookReviewRepository myBookReviewRepository;

    @DisplayName("마이북과 도서를 통해 마이북 리뷰가 존재하는지 확인한다.")
    @Test
    void existsByMyBookAndBook() {

        // given
        Book book = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBook());
        MyBook myBook = entityManager.persist(
                MyBookFixture.MY_BOOK_WITHOUT_RELATION.getMyBookBuilder().book(book).build());

        entityManager.persist(MyBookReviewFixture.MY_BOOK_REVIEW_WITHOUT_RELATION.getMyBookReviewBuilder()
                .book(book).myBook(myBook).build());

        entityManager.flush();
        entityManager.clear();

        // when
        boolean result = myBookReviewRepository.existsByMyBookAndBook(myBook, book);

        // then
        assertAll(
                () -> assertThat(result).isTrue()
        );
    }

    @DisplayName("도서를 통해 마이북 리뷰를 조회한다.")
    @Test
    void findReviewsByBook() {

        // given
        Book book = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBook());
        MyBook myBook = entityManager.persist(
                MyBookFixture.MY_BOOK_WITHOUT_RELATION.getMyBookBuilder().book(book).build());

        MyBookReview myBookReview = entityManager.persist(MyBookReviewFixture.MY_BOOK_REVIEW_WITHOUT_RELATION
                .getMyBookReviewBuilder().book(book).myBook(myBook).build());

        entityManager.flush();
        entityManager.clear();

        // when
        List<MyBookReviewElementDto> reviewsByBook = myBookReviewRepository.findReviewsByBook(book);

        // then
        assertAll(
                () -> assertThat(reviewsByBook).hasSize(1),
                () -> {
                    assert reviewsByBook != null;
                    assertThat(reviewsByBook.get(0).getId()).isEqualTo(myBookReview.getId());
                    assertThat(reviewsByBook.get(0).getUserId()).isEqualTo(myBook.getUserId());
                    assertThat(reviewsByBook.get(0).getContent()).isEqualTo(myBookReview.getContent());
                    assertThat(reviewsByBook.get(0).getCreatedAt()).isEqualTo(myBookReview.getCreatedAt());
                    assertThat(reviewsByBook.get(0).getStarRating()).isEqualTo(myBookReview.getStarRating());
                }
        );
    }

    @DisplayName("마이북을 통해 마이북 리뷰를 조회한다.")
    @Test
    void findReviewByMyBook() {

        // given
        Book book = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBook());
        MyBook myBook = entityManager.persist(
                MyBookFixture.MY_BOOK_WITHOUT_RELATION.getMyBookBuilder().book(book).build());

        MyBookReview myBookReview = entityManager.persist(MyBookReviewFixture.MY_BOOK_REVIEW_WITHOUT_RELATION
                .getMyBookReviewBuilder().book(book).myBook(myBook).build());

        entityManager.flush();
        entityManager.clear();

        // when
        Optional<ReviewFromMyBookModel> reviewByMyBook = myBookReviewRepository.findReviewByMyBook(myBook);

        // then
        assertAll(
                () -> {
                    assertThat(reviewByMyBook.isPresent()).isTrue();
                    assertThat(reviewByMyBook.get().getId()).isEqualTo(myBookReview.getId());
                    assertThat(reviewByMyBook.get().getContent()).isEqualTo(myBookReview.getContent());
                    assertThat(reviewByMyBook.get().getStarRating()).isEqualTo(myBookReview.getStarRating());
                    assertThat(reviewByMyBook.get().getCreatedAt()).isEqualTo(myBookReview.getCreatedAt());
                    assertThat(reviewByMyBook.get().getUpdatedAt()).isEqualTo(myBookReview.getUpdatedAt());
                }
        );
    }
}