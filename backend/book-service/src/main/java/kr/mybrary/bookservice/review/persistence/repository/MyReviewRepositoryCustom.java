package kr.mybrary.bookservice.review.persistence.repository;

import java.util.List;
import java.util.Optional;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.persistence.model.MyReviewFromMyBookModel;
import kr.mybrary.bookservice.review.persistence.model.MyReviewElementModel;

public interface MyReviewRepositoryCustom {

    List<MyReviewElementModel> findReviewsByBook(Book book);

    Optional<MyReviewFromMyBookModel> findReviewByMyBook(MyBook myBook);
}
