package kr.mybrary.bookservice.review;

import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.persistence.MyBookReview;
import kr.mybrary.bookservice.review.persistence.MyBookReview.MyBookReviewBuilder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MyBookReviewFixture {

    MY_BOOK_REVIEW_WITHOUT_RELATION(null, null, null, "마이 리뷰 내용입니다.", 4.5, false);

    private final Long id;
    private final MyBook myBook;
    private final Book book;
    private final String content;
    private final Double starRating;
    private final boolean deleted;

    public MyBookReview getMyBookReview() {
        return MyBookReview.builder()
                .id(id)
                .myBook(myBook)
                .book(book)
                .content(content)
                .starRating(starRating)
                .deleted(deleted)
                .build();
    }

    public MyBookReviewBuilder getMyBookReviewBuilder() {
        return MyBookReview.builder()
                .id(id)
                .myBook(myBook)
                .book(book)
                .content(content)
                .starRating(starRating)
                .deleted(deleted);
    }
}
