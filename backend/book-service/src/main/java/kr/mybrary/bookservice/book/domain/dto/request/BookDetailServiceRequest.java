package kr.mybrary.bookservice.book.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookDetailServiceRequest {

    private String loginId;
    private String isbn10;
    private String isbn13;

    public static BookDetailServiceRequest of(String loginId, String isbn10, String isbn13) {
        return BookDetailServiceRequest.builder()
                .loginId(loginId)
                .isbn10(isbn10)
                .isbn13(isbn13)
                .build();
    }
}
