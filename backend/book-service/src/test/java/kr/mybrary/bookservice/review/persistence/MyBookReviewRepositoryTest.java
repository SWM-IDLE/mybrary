package kr.mybrary.bookservice.review.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kr.mybrary.bookservice.PersistenceTest;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.MyBookFixture;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.MyBookReviewFixture;
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
}