package kr.mybrary.bookservice.book.domain.dto;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookSearchResultDto {

    private String title;
    private String description;
    private String detailsUrl;
    private String isbn10;
    private String isbn13;
    private List<String> authors;
    private List<String> translators;
    private String publisher;
    private String thumbnailUrl;
    private OffsetDateTime publicationDate;
    private Integer price;
    private Integer salePrice;

    private String status;
    private Double starRating;
}
