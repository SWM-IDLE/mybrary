package kr.mybrary.bookservice.mybook.persistence;

import static kr.mybrary.bookservice.mybook.persistence.QMyBook.myBook;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MyBookOrderType {

    NONE(new OrderSpecifier<>(Order.ASC, myBook.id)),
    TITLE(new OrderSpecifier<>(Order.ASC, myBook.book.title)),
    REGISTRATION(new OrderSpecifier<>(Order.DESC, myBook.createdAt)),
    PUBLICATION(new OrderSpecifier<>(Order.DESC, myBook.book.publicationDate));

    private final OrderSpecifier<?> orderSpecifier;

    public OrderSpecifier<?> getOrderSpecifier() {
        return orderSpecifier;
    }

    public static MyBookOrderType of(String orderType) {

        for (MyBookOrderType value : values()) {
            if (value.name().equals(orderType.toUpperCase())) {
                return value;
            }
        }

        return NONE;
    }
}
