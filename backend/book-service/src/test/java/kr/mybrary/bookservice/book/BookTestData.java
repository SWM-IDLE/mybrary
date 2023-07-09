package kr.mybrary.bookservice.book;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.author.Author;
import kr.mybrary.bookservice.book.persistence.author.BookAuthor;
import kr.mybrary.bookservice.book.persistence.translator.BookTranslator;
import kr.mybrary.bookservice.book.persistence.translator.Translator;

public class BookTestData {

    public static Book createBook() {
        return Book.builder()
                .title("test_title")
                .isbn10(UUID.randomUUID().toString().substring(0, 10))
                .isbn13(UUID.randomUUID().toString().substring(0, 13))
                .description("test_description")
                .publisher("test_publisher")
                .publishDate(LocalDateTime.now())
                .price(10000)
                .thumbnailUrl("test_thumbnailUrl")
                .detailsUrl("test_detailsUrl")
                .holderCount(1)
                .bookAuthors(createBookAuthors())
                .bookTranslators(createBookTranslators())
                .interestCount(1)
                .readCount(1)
                .build();
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
