package kr.mybrary.bookservice.book;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.Book.BookBuilder;
import kr.mybrary.bookservice.book.persistence.author.Author;
import kr.mybrary.bookservice.book.persistence.author.BookAuthor;
import kr.mybrary.bookservice.book.persistence.translator.BookTranslator;
import kr.mybrary.bookservice.book.persistence.translator.Translator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BookFixture {

    COMMON_BOOK(1L, "title", "description", "detailsUrl", "isbn10", "isbn13", "publisher",
            LocalDateTime.now(), 10000, "thumbnailUrl", 0, 0, 0, createBookAuthors(),
            createBookTranslators());

    private final Long id;
    private final String title;
    private final String description;
    private final String detailsUrl;
    private final String isbn10;
    private final String isbn13;
    private final String publisher;
    private final LocalDateTime publishDate;
    private final Integer price;
    private final String thumbnailUrl;
    private final Integer holderCount;
    private final Integer readCount;
    private final Integer interestCount;
    private final List<BookAuthor> bookAuthors;
    private final List<BookTranslator> bookTranslators;

    public Book getBook() {
        return Book.builder()
                .id(id)
                .title(title)
                .description(description)
                .detailsUrl(detailsUrl)
                .isbn10(isbn10)
                .isbn13(isbn13)
                .publisher(publisher)
                .publishDate(publishDate)
                .price(price)
                .thumbnailUrl(thumbnailUrl)
                .holderCount(holderCount)
                .readCount(readCount)
                .interestCount(interestCount)
                .bookAuthors(bookAuthors)
                .bookTranslators(bookTranslators)
                .build();
    }

    public BookBuilder getBookBuilder() {
        return Book.builder()
                .id(id)
                .title(title)
                .description(description)
                .detailsUrl(detailsUrl)
                .isbn10(isbn10)
                .isbn13(isbn13)
                .publisher(publisher)
                .publishDate(publishDate)
                .price(price)
                .thumbnailUrl(thumbnailUrl)
                .holderCount(holderCount)
                .readCount(readCount)
                .interestCount(interestCount)
                .bookAuthors(bookAuthors)
                .bookTranslators(bookTranslators);
    }



    private static List<BookAuthor> createBookAuthors() {
        BookAuthor testAuthor_1 = BookAuthor.builder()
                .author(Author.builder()
                        .name("test_author_1")
                        .build())
                .build();

        BookAuthor testAuthor_2 = BookAuthor.builder()
                .author(Author.builder()
                        .name("test_author_2")
                        .build())
                .build();

        return List.of(testAuthor_1, testAuthor_2);
    }

    private static List<BookTranslator> createBookTranslators() {
        BookTranslator testTranslator_1 = BookTranslator.builder()
                .translator(Translator.builder()
                        .name("test_translator_1")
                        .build())
                .build();

        BookTranslator testTranslator_2 = BookTranslator.builder()
                .translator(Translator.builder()
                        .name("test_translator_2")
                        .build())
                .build();

        return List.of(testTranslator_1, testTranslator_2);
    }
}
