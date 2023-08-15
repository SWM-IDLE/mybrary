package kr.mybrary.bookservice.booksearch.presentation.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookSearchResultResponse {

    private List<BookSearchResultResponseElement> bookSearchResult;
    private String nextRequestUrl;

    public static BookSearchResultResponse of(List<BookSearchResultResponseElement> bookSearchResult, String nextRequestUrl) {
        return BookSearchResultResponse.builder()
                .bookSearchResult(bookSearchResult)
                .nextRequestUrl(nextRequestUrl)
                .build();
    }
}
