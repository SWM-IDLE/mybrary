package kr.mybrary.bookservice.book.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookInterestElementResponse {

    private Long id;
    private String title;
    private String isbn13;
    private String thumbnailUrl;
    private String author;
}
