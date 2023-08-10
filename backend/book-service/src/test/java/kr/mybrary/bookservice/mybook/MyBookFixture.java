package kr.mybrary.bookservice.mybook;

import java.time.LocalDateTime;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.mybook.persistence.MyBook.MyBookBuilder;
import kr.mybrary.bookservice.mybook.persistence.ReadStatus;
import kr.mybrary.bookservice.review.MyReviewFixture;
import kr.mybrary.bookservice.review.persistence.MyReview;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MyBookFixture {

    COMMON_LOGIN_USER_MYBOOK(1L, "LOGIN_USER_ID", BookFixture.COMMON_BOOK.getBook(), null, ReadStatus.TO_READ,
            LocalDateTime.now(), true, false, false, false),
    DELETED_LOGIN_USER_MYBOOK(2L, "LOGIN_USER_ID", BookFixture.COMMON_BOOK.getBook(), null, ReadStatus.TO_READ,
            LocalDateTime.now(), true, false, false, true),
    NOT_SHOWABLE_LOGIN_USER_MYBOOK(3L, "LOGIN_USER_ID", BookFixture.COMMON_BOOK.getBook(), null, ReadStatus.TO_READ,
            LocalDateTime.now(), false, false, false, false),
    NOT_SHOWABLE_OTHER_USER_MYBOOK(4L, "OTHER_USER_ID", BookFixture.COMMON_BOOK.getBook(), null, ReadStatus.TO_READ,
            LocalDateTime.now(), false, false, false, false),
    COMMON_OTHER_USER_MYBOOK(5L, "OTHER_USER_ID", BookFixture.COMMON_BOOK.getBook(), null, ReadStatus.TO_READ,
            LocalDateTime.now(), true, false, false, false),
    MYBOOK_WITH_REVIEW(6L, "LOGIN_USER_ID", BookFixture.COMMON_BOOK.getBook(), MyReviewFixture.COMMON_MY_BOOK_REVIEW.getMyBookReview(), ReadStatus.TO_READ,
            LocalDateTime.now(), true, false, false, false),
    MY_BOOK_WITHOUT_RELATION(null, "LOGIN_USER_ID", null, null, ReadStatus.TO_READ, LocalDateTime.now(),
            true, false, false, false);

    private final Long id;
    private final String userId;
    private final Book book;
    private final MyReview myReview;
    private final ReadStatus readStatus;
    private final LocalDateTime startDateOfPossession;
    private final boolean showable;
    private final boolean exchangeable;
    private final boolean shareable;
    private final boolean deleted;

    public MyBook getMyBook() {
        return MyBook.builder()
                .id(id)
                .userId(userId)
                .book(book)
                .readStatus(readStatus)
                .startDateOfPossession(startDateOfPossession)
                .showable(showable)
                .exchangeable(exchangeable)
                .shareable(shareable)
                .deleted(deleted)
                .build();
    }

    public MyBookBuilder getMyBookBuilder() {
        return MyBook.builder()
                .id(id)
                .userId(userId)
                .book(book)
                .myReview(myReview)
                .readStatus(readStatus)
                .startDateOfPossession(startDateOfPossession)
                .showable(showable)
                .exchangeable(exchangeable)
                .shareable(shareable)
                .deleted(deleted);
    }

    public MyBook getMyBookWithBook(Book book) {
        return MyBook.builder()
                .id(id)
                .userId(userId)
                .book(book)
                .myReview(myReview)
                .readStatus(readStatus)
                .startDateOfPossession(startDateOfPossession)
                .showable(showable)
                .exchangeable(exchangeable)
                .shareable(shareable)
                .deleted(deleted)
                .build();
    }
}
