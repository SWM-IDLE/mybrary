package kr.mybrary.book.domain.dto.request;

import kr.mybrary.book.persistence.BookOrderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookMyInterestFindServiceRequest {

    private String loginId;
    private BookOrderType bookOrderType;

    public static BookMyInterestFindServiceRequest of(String loginId, BookOrderType bookOrderType) {
        return BookMyInterestFindServiceRequest.builder()
                .loginId(loginId)
                .bookOrderType(bookOrderType)
                .build();
    }
}
