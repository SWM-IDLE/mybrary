package kr.mybrary.bookservice.book.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String description;

    private String isbn10;

    private String isbn13;

    private String publisher;

    private LocalDateTime publishDate;

    private Integer price;

    private String thumbnailUrl;

    private Integer holderCount;

    private Integer readCount;

    private Integer interestCount;

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
    private List<BookAuthor> bookAuthors = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
    private List<BookTranslator> bookTranslators = new ArrayList<>();

    public void addBookAuthorAndBookTranslator(List<BookAuthor> bookAuthorList,
            List<BookTranslator> bookTranslatorList) {

        bookAuthorList.forEach(bookAuthor -> {
            this.bookAuthors.add(bookAuthor);
            bookAuthor.assignBook(this);
        });

        bookTranslatorList.forEach(bookTranslator -> {
            this.bookTranslators.add(bookTranslator);
            bookTranslator.assignBook(this);
        });
    }
}
