package kr.mybrary.bookservice.book.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookInterestStatusServiceRequest {

    private String isbn13;
    private String loginId;

}
