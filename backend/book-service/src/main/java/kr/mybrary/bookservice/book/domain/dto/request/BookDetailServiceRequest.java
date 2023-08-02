package kr.mybrary.bookservice.book.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookDetailServiceRequest {

    private String isbn10;
    private String isbn13;

    public static BookDetailServiceRequest of(String isbn10, String isbn13) {
        return BookDetailServiceRequest.builder()
                .isbn10(isbn10)
                .isbn13(isbn13)
                .build();
    }
}
