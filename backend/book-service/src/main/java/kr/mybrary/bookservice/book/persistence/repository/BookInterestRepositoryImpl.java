package kr.mybrary.bookservice.book.persistence.repository;

import static kr.mybrary.bookservice.book.persistence.QBook.book;
import static kr.mybrary.bookservice.book.persistence.QBookInterest.bookInterest;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import kr.mybrary.bookservice.book.persistence.BookInterest;
import kr.mybrary.bookservice.book.persistence.OrderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookInterestRepositoryImpl implements BookInterestRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BookInterest> findAllByUserIdWithBook(String userId, OrderType orderType) {

        return queryFactory
                .selectFrom(bookInterest)
                .join(bookInterest.book, book).fetchJoin()
                .where(bookInterest.userId.eq(userId))
                .orderBy(createOrderSpecifiers(orderType))
                .fetch();
    }

    private OrderSpecifier<?> createOrderSpecifiers(OrderType orderType) {
        if (Objects.requireNonNull(orderType) == OrderType.INITIAL) {
            return new OrderSpecifier<>(Order.ASC, bookInterest.book.title);
        } else if (orderType == OrderType.REGISTRATION) {
            return new OrderSpecifier<>(Order.DESC, bookInterest.createdAt);
        } else if (orderType == OrderType.PUBLICATION) {
            return new OrderSpecifier<>(Order.DESC, bookInterest.book.publicationDate);
        }
        return new OrderSpecifier<>(Order.ASC, bookInterest.id);
    }
}
