package kr.mybrary.bookservice.book.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookInterestStatusServiceRequest {

    private String loginId;
    private String isbn13;

    public static BookInterestStatusServiceRequest of(String loginId, String isbn13) {
        return BookInterestStatusServiceRequest.builder()
                .loginId(loginId)
                .isbn13(isbn13)
                .build();
    }

}
