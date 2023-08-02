package kr.mybrary.bookservice.book.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kr.mybrary.bookservice.book.persistence.author.BookAuthor;
import kr.mybrary.bookservice.book.persistence.translator.BookTranslator;
import kr.mybrary.bookservice.global.BaseEntity;
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
    private String thumbnailUrl;
    private String link;
    @Column(unique = true)
    private String isbn10;

    @Column(unique = true, nullable = false)
    private String isbn13;

    private Integer pages;
    private String publisher;
    private LocalDateTime publicationDate;

    @Column(length = 4000)
    private String description;
    @Column(length = 4000)
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
}
