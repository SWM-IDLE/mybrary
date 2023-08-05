package kr.mybrary.bookservice.book.persistence.repository;

import java.util.List;
import kr.mybrary.bookservice.book.persistence.BookInterest;
import kr.mybrary.bookservice.book.persistence.BookOrderType;

public interface BookInterestRepositoryCustom {

    List<BookInterest> findAllByUserIdWithBook(String userId, BookOrderType bookOrderType);

}
