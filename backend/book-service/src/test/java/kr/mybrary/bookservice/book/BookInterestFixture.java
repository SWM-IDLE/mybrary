package kr.mybrary.bookservice.book;

import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.BookInterest;
import kr.mybrary.bookservice.book.persistence.BookInterest.BookInterestBuilder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BookInterestFixture {

    COMMON_BOOK_INTEREST(1L, "LOGIN_USER_ID", BookFixture.COMMON_BOOK.getBook()),
    BOOK_INTEREST_WITHOUT_RELATION(null, "LOGIN_USER_ID", null);

    private final Long id;
    private final String userId;
    private final Book book;

    public BookInterest getBookInterest() {
        return BookInterest.builder()
                .id(id)
                .userId(userId)
                .book(book)
                .build();
    }

    public BookInterestBuilder getBookInterestBuilder() {
         return BookInterest.builder()
                .id(id)
                .userId(userId)
                .book(book);
    }
}
