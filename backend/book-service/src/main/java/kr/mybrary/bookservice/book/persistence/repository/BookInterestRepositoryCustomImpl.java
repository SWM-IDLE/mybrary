package kr.mybrary.bookservice.book.persistence.repository;

import static kr.mybrary.bookservice.book.persistence.QBook.book;
import static kr.mybrary.bookservice.book.persistence.QBookInterest.bookInterest;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Arrays;
import java.util.List;
import kr.mybrary.bookservice.book.persistence.BookInterest;
import kr.mybrary.bookservice.book.persistence.BookOrderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookInterestRepositoryCustomImpl implements BookInterestRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BookInterest> findAllByUserIdWithBook(String userId, BookOrderType bookOrderType) {

        return queryFactory
                .selectFrom(bookInterest)
                .join(bookInterest.book, book).fetchJoin()
                .where(bookInterest.userId.eq(userId))
                .orderBy(createOrderType(bookOrderType))
                .fetch();
    }

    private OrderSpecifier<?> createOrderType(BookOrderType bookOrderType) {

        return Arrays.stream(BookOrderType.values())
                .filter(orderType -> orderType == bookOrderType)
                .findFirst()
                .orElseGet(() -> BookOrderType.NONE)
                .getOrderSpecifier();
    }
}
