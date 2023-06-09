package kr.mybrary.bookservice.book.domain.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookCreateServiceRequest {
    private String title;
    private String description;
    private String detailsUrl;
    private String isbn10;
    private String isbn13;
    private String publisher;
    private LocalDateTime publicationDate;
    private Integer price;
    private String thumbnailUrl;

    private List<String> authors;
    private List<String> translators;

}
