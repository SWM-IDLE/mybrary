package kr.mybrary.bookservice.book.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookAuthor;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookCategory;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookTranslator;
import kr.mybrary.bookservice.global.BaseEntity;
import kr.mybrary.bookservice.mybook.persistence.ReadStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String subTitle;
    private String author;
    private String thumbnailUrl;
    private String link;
    @Column(unique = true)
    private String isbn10;

    @Column(unique = true, nullable = false)
    private String isbn13;

    private Integer pages;
    private String publisher;
    private LocalDateTime publicationDate;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String toc;

    private Integer weight;
    private Integer sizeDepth;
    private Integer sizeHeight;
    private Integer sizeWidth;
    private Integer priceSales;
    private Integer priceStandard;

    private Integer holderCount;
    private Integer readCount;
    private Integer interestCount;
    private Double starRating;
    private Integer reviewCount;

    private Double aladinStarRating;
    private Integer aladinReviewCount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private BookCategory bookCategory;

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
    private List<BookAuthor> bookAuthors = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
    private List<BookTranslator> bookTranslators = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "book")
    private List<BookInterest> bookInterests = new ArrayList<>();

    public void addBookAuthor(List<BookAuthor> bookAuthorList) {
        bookAuthorList.forEach(bookAuthor -> {
            this.bookAuthors.add(bookAuthor);
            bookAuthor.assignBook(this);
        });
    }

    public void addBookTranslator(List<BookTranslator> bookTranslatorList) {
        bookTranslatorList.forEach(bookTranslator -> {
            this.bookTranslators.add(bookTranslator);
            bookTranslator.assignBook(this);
        });
    }

    public void assignCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }

    public void increaseHolderCount() {
        this.holderCount++;
    }

    public void decreaseHolderCount() {
        this.holderCount--;
    }

    public void increaseInterestCount() {
        this.interestCount++;
    }

    public void decreaseInterestCount() {
        this.interestCount--;
    }

    private void increaseReadCount() {
        this.readCount++;
    }

    private void decreaseReadCount() {
        this.readCount--;
    }

    public void adjustReadCount(ReadStatus previousReadStatus, ReadStatus currentReadStatus) {

        if (previousReadStatus == ReadStatus.COMPLETED && currentReadStatus != ReadStatus.COMPLETED) {
            this.decreaseReadCount();
        } else if (previousReadStatus != ReadStatus.COMPLETED && currentReadStatus == ReadStatus.COMPLETED) {
            this.increaseReadCount();
        }
    }

    public void updateWhenCreateReview(Double newStartRating) {
        this.reviewCount++;
        this.starRating += newStartRating;
    }

    public void updateWhenUpdateReview(Double originStarRating, Double newStarRating) {
        this.starRating = (this.starRating - originStarRating) + newStarRating;
    }

    public void updateWhenDeleteReview(Double originStarRating) {
        this.reviewCount--;
        this.starRating -= originStarRating;
    }
}
