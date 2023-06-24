package kr.mybrary.bookservice.book.presentation.dto.response;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookSearchResultResponse {

    private String title;
    private String contents;
    private List<String> authors;
    private String thumbnailUrl;
    private OffsetDateTime publicationDate;
    private Double starRating;

    private Integer price;
    private String status;
}
