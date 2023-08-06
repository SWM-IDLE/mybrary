package kr.mybrary.bookservice.review.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.global.BaseEntity;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.domain.dto.request.MyBookReviewCreateServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false")
public class MyBookReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private MyBook myBook;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    @Column(length = 500)
    private String content;

    private Double starRating;
    private boolean deleted;

    public static MyBookReview of(MyBook myBook, MyBookReviewCreateServiceRequest request) {
        return MyBookReview.builder()
                .myBook(myBook)
                .book(myBook.getBook())
                .content(request.getContent())
                .starRating(request.getStarRating())
                .build();
    }
}
