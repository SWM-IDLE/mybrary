package kr.mybrary.bookservice.mybook.domain.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyBookCreateServiceRequest {

    private String userId;

    private String title;
    private String description;
    private String isbn10;
    private String isbn13;
    private String publisher;
    private LocalDateTime publicationDate;
    private Integer price;
    private String thumbnailUrl;

    private List<String> authors;
    private List<String> translators;

    public BookCreateServiceRequest toBookCreateRequest() {
        return BookCreateServiceRequest.builder()
                .title(title)
                .description(description)
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
