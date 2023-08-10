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
import kr.mybrary.bookservice.review.MyReviewFixture;
import kr.mybrary.bookservice.review.persistence.model.MyReviewElementModel;
import kr.mybrary.bookservice.review.persistence.model.MyReviewFromMyBookModel;
import kr.mybrary.bookservice.review.persistence.repository.MyReviewRepository;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@PersistenceTest
class MyReviewRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MyReviewRepository myBookReviewRepository;

    @DisplayName("마이북과 도서를 통해 마이북 리뷰가 존재하는지 확인한다.")
    @Test
    void existsByMyBookAndBook() {

        // given
        Book book = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBook());
        MyBook myBook = entityManager.persist(
                MyBookFixture.MY_BOOK_WITHOUT_RELATION.getMyBookBuilder().book(book).build());

        entityManager.persist(MyReviewFixture.MY_BOOK_REVIEW_WITHOUT_RELATION.getMyBookReviewBuilder()
                .book(book).myBook(myBook).build());

        entityManager.flush();
        entityManager.clear();

        // when
        boolean result = myBookReviewRepository.existsByMyBook(myBook);

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

        MyReview myReview = entityManager.persist(MyReviewFixture.MY_BOOK_REVIEW_WITHOUT_RELATION
                .getMyBookReviewBuilder().book(book).myBook(myBook).build());

        entityManager.flush();
        entityManager.clear();

        // when
        List<MyReviewElementModel> reviewsByBook = myBookReviewRepository.findReviewsByBook(book);

        // then
        assertAll(
                () -> assertThat(reviewsByBook).hasSize(1),
                () -> {
                    assert reviewsByBook != null;
                    assertThat(reviewsByBook.get(0).getId()).isEqualTo(myReview.getId());
                    assertThat(reviewsByBook.get(0).getUserId()).isEqualTo(myBook.getUserId());
                    assertThat(reviewsByBook.get(0).getContent()).isEqualTo(myReview.getContent());
                    assertThat(reviewsByBook.get(0).getCreatedAt()).isNotNull();
                    assertThat(reviewsByBook.get(0).getStarRating()).isEqualTo(myReview.getStarRating());
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

        MyReview myReview = entityManager.persist(MyReviewFixture.MY_BOOK_REVIEW_WITHOUT_RELATION
                .getMyBookReviewBuilder().book(book).myBook(myBook).build());

        entityManager.flush();
        entityManager.clear();

        // when
        Optional<MyReviewFromMyBookModel> reviewByMyBook = myBookReviewRepository.findReviewByMyBook(myBook);

        // then
        assertAll(
                () -> {
                    assertThat(reviewByMyBook.isPresent()).isTrue();
                    assertThat(reviewByMyBook.get().getId()).isEqualTo(myReview.getId());
                    assertThat(reviewByMyBook.get().getContent()).isEqualTo(myReview.getContent());
                    assertThat(reviewByMyBook.get().getStarRating()).isEqualTo(myReview.getStarRating());
                    assertThat(reviewByMyBook.get().getCreatedAt()).isNotNull();
                    assertThat(reviewByMyBook.get().getUpdatedAt()).isNotNull();
                }
        );
    }

    @DisplayName("마이 리뷰 조회시 연관되어 있는 마이북을 함께 조회한다.")
    @Test
    void findByIdWithMyBookUsingFetchJoin() {

        // given
        Book book = entityManager.persist(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBook());
        MyBook myBook = entityManager.persist(
                MyBookFixture.MY_BOOK_WITHOUT_RELATION.getMyBookBuilder().book(book).build());

        MyReview myReview = entityManager.persist(MyReviewFixture.MY_BOOK_REVIEW_WITHOUT_RELATION
                .getMyBookReviewBuilder().book(book).myBook(myBook).build());

        entityManager.flush();
        entityManager.clear();

        // when
        myBookReviewRepository.findByIdWithMyBookUsingFetchJoin(myReview.getId()).ifPresent(review -> {

            // then
            assertAll(
                    () -> assertThat(review.getId()).isEqualTo(myReview.getId()),
                    () -> assertThat(review.getContent()).isEqualTo(myReview.getContent()),
                    () -> assertThat(review.getStarRating()).isEqualTo(myReview.getStarRating()),
                    () -> assertThat(review.getMyBook()).isNotInstanceOf(HibernateProxy.class)
            );
        });

    }
}