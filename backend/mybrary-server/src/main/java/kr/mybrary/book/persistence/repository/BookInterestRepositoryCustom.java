package kr.mybrary.book.persistence.repository;

import java.util.List;
import kr.mybrary.book.persistence.BookInterest;
import kr.mybrary.book.persistence.BookOrderType;

public interface BookInterestRepositoryCustom {

    List<BookInterest> findAllByUserIdWithBook(String userId, BookOrderType bookOrderType);

}
