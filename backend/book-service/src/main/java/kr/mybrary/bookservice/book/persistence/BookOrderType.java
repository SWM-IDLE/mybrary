package kr.mybrary.bookservice.book.persistence;

import static kr.mybrary.bookservice.book.persistence.QBookInterest.bookInterest;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookOrderType {

    NONE(new OrderSpecifier<>(Order.ASC, bookInterest.id)),
    TITLE(new OrderSpecifier<>(Order.ASC, bookInterest.book.title)),
    REGISTRATION(new OrderSpecifier<>(Order.DESC, bookInterest.createdAt)),
    PUBLICATION(new OrderSpecifier<>(Order.DESC, bookInterest.book.publicationDate));

    private final OrderSpecifier<?> orderSpecifier;

    public OrderSpecifier<?> getOrderSpecifier() {
        return orderSpecifier;
    }

    public static BookOrderType of(String orderType) {

        for (BookOrderType value : values()) {
            if (value.name().equals(orderType.toUpperCase())) {
                return value;
            }
        }

        return NONE;
    }
}
