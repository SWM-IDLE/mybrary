package kr.mybrary.bookservice.mybook.presentation.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookCreateRequest {
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

    public MyBookCreateServiceRequest toServiceRequest(String userId) {
        return MyBookCreateServiceRequest.builder()
                .userId(userId)
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
