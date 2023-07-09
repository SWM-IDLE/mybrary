package kr.mybrary.bookservice.book;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.mybrary.bookservice.book.persistence.Book;

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
                .bookTranslators(List.of())
                .bookAuthors(List.of())
                .interestCount(1)
                .readCount(1)
                .build();
    }
}
