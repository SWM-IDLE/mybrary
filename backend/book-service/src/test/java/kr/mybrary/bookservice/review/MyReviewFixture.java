package kr.mybrary.bookservice.review;

import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.persistence.MyReview;
import kr.mybrary.bookservice.review.persistence.MyReview.MyReviewBuilder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MyReviewFixture {

    COMMON_MY_BOOK_REVIEW(1L, null, null, "마이 리뷰 내용입니다.", 4.5, false),
    MY_BOOK_REVIEW_WITHOUT_RELATION(null, null, null, "마이 리뷰 내용입니다.", 4.5, false);

    private final Long id;
    private final MyBook myBook;
    private final Book book;
    private final String content;
    private final Double starRating;
    private final boolean deleted;

    public MyReview getMyBookReview() {
        return MyReview.builder()
                .id(id)
                .myBook(myBook)
                .book(book)
                .content(content)
                .starRating(starRating)
                .deleted(deleted)
                .build();
    }

    public MyReviewBuilder getMyBookReviewBuilder() {
        return MyReview.builder()
                .id(id)
                .myBook(myBook)
                .book(book)
                .content(content)
                .starRating(starRating)
                .deleted(deleted);
    }
}
