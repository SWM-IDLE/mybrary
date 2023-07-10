package kr.mybrary.bookservice.book;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.book.presentation.dto.request.BookCreateRequest;

public class BookDtoTestData {

    public static BookCreateRequest createBookCreateRequest() {
        return BookCreateRequest.builder()
                .title("title")
                .description("description")
                .detailsUrl("detailUrl")
                .isbn10("isbn10")
                .isbn13("isbn13")
                .publisher("publisher")
                .price(10000)
                .publicationDate(LocalDateTime.now())
                .translators(List.of("translator1", "translator2"))
                .authors(List.of("author1", "author2"))
                .thumbnailUrl("thumbnailUrl")
                .build();
    }
}
