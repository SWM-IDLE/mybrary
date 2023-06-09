package kr.mybrary.bookservice.book.presentation.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookCreateRequest {

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

    public BookCreateServiceRequest toServiceRequest() {
        return BookCreateServiceRequest.builder()
                .title(title)
                .description(description)
                .detailsUrl(detailsUrl)
                .isbn10(isbn10)
                .isbn13(isbn13)
                .publisher(publisher)
                .publicationDate(publicationDate)
                .price(price)
                .thumbnailUrl(thumbnailUrl)
                .authors(authors)
                .translators(translators)
                .build();
    }
}
