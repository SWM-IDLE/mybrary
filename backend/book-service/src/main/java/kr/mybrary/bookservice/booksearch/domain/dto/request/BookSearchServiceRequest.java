package kr.mybrary.bookservice.booksearch.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchServiceRequest {

    private String keyword;
    private String sort;
    private int page;

    public static BookSearchServiceRequest of(String keyword, String sort, int page) {
        return BookSearchServiceRequest.builder()
                .keyword(keyword)
                .sort(sort)
                .page(page)
                .build();
    }

    public static BookSearchServiceRequest of(String isbn) {
        return BookSearchServiceRequest.builder()
                .keyword(isbn)
                .sort("accuracy")
                .page(1)
                .build();
    }
}
