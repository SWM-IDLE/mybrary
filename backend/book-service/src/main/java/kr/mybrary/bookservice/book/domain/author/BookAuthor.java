package kr.mybrary.bookservice.book.domain.author;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.mybrary.bookservice.book.domain.BaseEntity;
import kr.mybrary.bookservice.book.domain.Book;
import lombok.Getter;

@Entity
@Table(name = "books_authors")
@Getter
public class BookAuthor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    public void assignBook(Book book) {
        this.book = book;
    }
}
