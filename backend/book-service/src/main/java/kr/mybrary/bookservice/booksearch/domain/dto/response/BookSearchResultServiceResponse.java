package kr.mybrary.bookservice.booksearch.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookSearchResultServiceResponse {

    private String title;
    private String description;
    private String author;
    private String isbn13;
    private String thumbnailUrl;
    private String publicationDate;
    private Double starRating;
}
