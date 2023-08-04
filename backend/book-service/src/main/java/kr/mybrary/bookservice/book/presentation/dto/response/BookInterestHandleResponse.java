package kr.mybrary.bookservice.book.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookInterestHandleResponse {

    private String userId;
    private String isbn13;
    private boolean interested;
}
