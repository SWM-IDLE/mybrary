package kr.mybrary.review.persistence.repository;

import java.util.List;
import java.util.Optional;
import kr.mybrary.book.persistence.Book;
import kr.mybrary.mybook.persistence.MyBook;
import kr.mybrary.review.persistence.model.MyReviewFromMyBookModel;
import kr.mybrary.review.persistence.model.MyReviewElementModel;

public interface MyReviewRepositoryCustom {

    List<MyReviewElementModel> findReviewsByBook(Book book);

    Optional<MyReviewFromMyBookModel> findReviewByMyBook(MyBook myBook);
}
