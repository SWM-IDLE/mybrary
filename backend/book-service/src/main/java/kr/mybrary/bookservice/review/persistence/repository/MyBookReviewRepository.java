package kr.mybrary.bookservice.review.persistence.repository;

import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.persistence.MyBookReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyBookReviewRepository extends JpaRepository<MyBookReview, Long> {

    boolean existsByMyBookAndBook(MyBook myBook, Book book);
}
