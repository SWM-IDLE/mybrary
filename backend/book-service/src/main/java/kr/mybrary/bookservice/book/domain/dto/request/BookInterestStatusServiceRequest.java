package kr.mybrary.bookservice.book.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookInterestStatusServiceRequest {

    private String isbn13;
    private String loginId;

    public static BookInterestStatusServiceRequest of(String isbn13, String loginId) {
        return BookInterestStatusServiceRequest.builder()
                .isbn13(isbn13)
                .loginId(loginId)
                .build();
    }

}
