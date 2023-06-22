package kr.mybrary.bookservice.book.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kr.mybrary.bookservice.book.domain.author.BookAuthor;
import kr.mybrary.bookservice.book.domain.translator.BookTranslator;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contents;

    private String descriptionUrl;

    private String isbn10;

    private String isbn13;

    private LocalDateTime publishDate;

    private Integer price;

    private String thumbnailUrl;

    private Integer holderCount;

    private Integer readCount;

    private Integer interestCount;

    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
    private List<BookAuthor> bookAuthors = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
    private List<BookTranslator> bookTranslators = new ArrayList<>();

    public void addBookAuthorAndBookTranslator(List<BookAuthor> bookAuthors,
            List<BookTranslator> bookTranslators) {

        bookAuthors.forEach(bookAuthor -> {
            this.getBookAuthors().add(bookAuthor);
            bookAuthor.assignBook(this);
        });

        bookTranslators.forEach(bookTranslator -> {
            this.getBookTranslators().add(bookTranslator);
            bookTranslator.assignBook(this);
        });
    }
}
