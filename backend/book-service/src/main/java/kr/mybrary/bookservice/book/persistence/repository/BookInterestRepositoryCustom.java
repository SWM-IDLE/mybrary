package kr.mybrary.bookservice.book.persistence.repository;

import java.util.List;
import kr.mybrary.bookservice.book.persistence.BookInterest;
import kr.mybrary.bookservice.book.persistence.OrderType;

public interface BookInterestRepositoryCustom {

    List<BookInterest> findAllByUserIdWithBook(String userId, OrderType orderType);

}
