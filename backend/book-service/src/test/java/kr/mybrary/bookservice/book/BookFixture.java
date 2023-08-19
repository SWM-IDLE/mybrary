package kr.mybrary.bookservice.book;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.Book.BookBuilder;
import kr.mybrary.bookservice.book.persistence.BookInterest;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookCategory;
import kr.mybrary.bookservice.book.persistence.bookInfo.Author;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookAuthor;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookTranslator;
import kr.mybrary.bookservice.book.persistence.bookInfo.Translator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BookFixture {

    COMMON_BOOK(1L, "title", "subTitle", "author","thumbnailUrl", "link", "isbn10", "isbn13", 100, "publisher",
            LocalDateTime.now(), "description", "toc", 10, 11, 12, 13, 10000, 11000, 1, 1, 1, 4.5, 2, 3.5, 10,
            createBookCategory(), createBookAuthors(), createBookTranslators(), createBookInterest()),

    COMMON_BOOK_WITHOUT_RELATION(null, "title", "author","subTitle", "thumbnailUrl", "link", "isbn10", "isbn13", 100, "publisher",
            LocalDateTime.now(), "description", "toc", 10, 11, 12, 13, 10000, 11000, 1, 1, 1, 4.5, 1, 3.5, 10,
            null, null, null, null);

    private final Long id;
    private final String title;
    private final String subTitle;
    private final String author;
    private final String thumbnailUrl;
    private final String link;
    private final String isbn10;
    private final String isbn13;
    private final Integer pages;
    private final String publisher;
    private final LocalDateTime publicationDate;
    private final String description;
    private final String toc;
    private final Integer weight;
    private final Integer sizeDepth;
    private final Integer sizeHeight;
    private final Integer sizeWidth;
    private final Integer priceSales;
    private final Integer priceStandard;
    private final Integer holderCount;
    private final Integer readCount;
    private final Integer interestCount;
    private final Double starRating;
    private final Integer reviewCount;
    private final Double aladinStarRating;
    private final Integer aladinReviewCount;
    private final BookCategory bookCategory;
    private final List<BookAuthor> bookAuthors;
    private final List<BookTranslator> bookTranslators;
    private final List<BookInterest> bookInterests;

    public Book getBook() {
        return Book.builder().id(id).title(title).subTitle(subTitle).author(author).thumbnailUrl(thumbnailUrl).link(link)
                .isbn10(isbn10).isbn13(isbn13).pages(pages).publisher(publisher).publicationDate(publicationDate)
                .description(description).toc(toc).weight(weight).sizeDepth(sizeDepth).sizeHeight(sizeHeight)
                .sizeWidth(sizeWidth).priceSales(priceSales).priceStandard(priceStandard).holderCount(holderCount)
                .readCount(readCount).interestCount(interestCount).starRating(starRating).reviewCount(reviewCount)
                .bookAuthors(bookAuthors).aladinStarRating(aladinStarRating).aladinReviewCount(aladinReviewCount)
                .bookCategory(bookCategory).bookTranslators(bookTranslators).bookInterests(bookInterests).build();
    }

    public BookBuilder getBookBuilder() {
        return Book.builder().id(id).title(title).subTitle(subTitle).author(author).thumbnailUrl(thumbnailUrl).link(link)
                .isbn10(isbn10).isbn13(isbn13).pages(pages).publisher(publisher).publicationDate(publicationDate)
                .description(description).toc(toc).weight(weight).sizeDepth(sizeDepth).sizeHeight(sizeHeight)
                .sizeWidth(sizeWidth).priceSales(priceSales).priceStandard(priceStandard).holderCount(holderCount)
                .readCount(readCount).interestCount(interestCount).starRating(starRating).reviewCount(reviewCount)
                .bookAuthors(bookAuthors).aladinStarRating(aladinStarRating).aladinReviewCount(aladinReviewCount)
                .bookCategory(bookCategory).bookTranslators(bookTranslators).bookInterests(bookInterests);
    }


    private static List<BookAuthor> createBookAuthors() {
        BookAuthor testAuthor_1 = BookAuthor.builder()
                .author(Author.builder().aid(10).name("test_author_1").build())
                .build();

        BookAuthor testAuthor_2 = BookAuthor.builder()
                .author(Author.builder().aid(11).name("test_author_2").build())
                .build();

        return new ArrayList<>(List.of(testAuthor_1, testAuthor_2));
    }

    private static List<BookTranslator> createBookTranslators() {
        BookTranslator testTranslator_1 = BookTranslator.builder()
                .translator(Translator.builder().tid(12).name("test_translator_1").build())
                .build();

        BookTranslator testTranslator_2 = BookTranslator.builder()
                .translator(Translator.builder().tid(13).name("test_translator_2").build())
                .build();

        return new ArrayList<>(List.of(testTranslator_1, testTranslator_2));
    }

    private static BookCategory createBookCategory() {
        return BookCategory
                .builder().cid(14).name("test_category")
                .build();
    }

    private static List<BookInterest> createBookInterest() {
        BookInterest bookInterest_1 = BookInterest
                .builder().book(Book.builder().build()).userId("LOGIN_USER_ID")
                .build();

        BookInterest bookInterest_2 = BookInterest
                .builder().book(Book.builder().build()).userId("OTHER_USER_ID")
                .build();

        return new ArrayList<>(List.of(bookInterest_1, bookInterest_2));
    }
}
