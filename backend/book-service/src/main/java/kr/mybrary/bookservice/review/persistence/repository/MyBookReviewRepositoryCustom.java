package kr.mybrary.bookservice.review.persistence.repository;

import java.util.List;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.review.persistence.dto.MyBookReviewElementDto;

public interface MyBookReviewRepositoryCustom {

    List<MyBookReviewElementDto> findReviewsByBook(Book book);

}
