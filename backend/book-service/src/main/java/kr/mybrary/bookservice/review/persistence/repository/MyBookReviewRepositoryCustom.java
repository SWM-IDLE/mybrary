package kr.mybrary.bookservice.review.persistence.repository;

import java.util.List;
import java.util.Optional;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.persistence.model.MyBookReviewElementDto;
import kr.mybrary.bookservice.review.persistence.model.ReviewFromMyBookModel;

public interface MyBookReviewRepositoryCustom {

    List<MyBookReviewElementDto> findReviewsByBook(Book book);

    Optional<ReviewFromMyBookModel> findReviewByMyBook(MyBook myBook);
}
