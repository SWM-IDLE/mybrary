package kr.mybrary.bookservice.book.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookDetailServiceRequest {

    private String isbn10;
    private String isbn13;

}
