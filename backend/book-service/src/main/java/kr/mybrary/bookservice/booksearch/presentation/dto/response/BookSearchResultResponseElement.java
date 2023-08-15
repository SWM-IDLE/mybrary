package kr.mybrary.bookservice.booksearch.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookSearchResultResponseElement {

    private String title;
    private String description;
    private String author;
    private String isbn13;
    private String thumbnailUrl;
    private String publicationDate;
    private Double starRating;
}
