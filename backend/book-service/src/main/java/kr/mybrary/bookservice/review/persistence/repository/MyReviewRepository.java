package kr.mybrary.bookservice.review.persistence.repository;

import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.persistence.MyReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyReviewRepository extends JpaRepository<MyReview, Long>, MyReviewRepositoryCustom {

    boolean existsByMyBookAndBook(MyBook myBook, Book book);
}
