package kr.mybrary.bookservice.booksearch.presentation.dto.response;

import java.util.List;
import kr.mybrary.bookservice.booksearch.domain.dto.response.BookSearchResultServiceResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookSearchResultResponse {

    private List<BookSearchResultServiceResponse> bookSearchResult;
    private String nextRequestUrl;

    public static BookSearchResultResponse of(List<BookSearchResultServiceResponse> bookSearchResult, String nextRequestUrl) {
        return BookSearchResultResponse.builder()
                .bookSearchResult(bookSearchResult)
                .nextRequestUrl(nextRequestUrl)
                .build();
    }
}
