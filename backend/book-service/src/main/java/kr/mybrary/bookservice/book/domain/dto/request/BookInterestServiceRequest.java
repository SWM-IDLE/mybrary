package kr.mybrary.bookservice.book.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookInterestServiceRequest {

    private String isbn13;
    private String loginId;

    public static BookInterestServiceRequest of(String isbn13, String loginId) {
        return BookInterestServiceRequest.builder()
                .isbn13(isbn13)
                .loginId(loginId)
                .build();
    }
}
