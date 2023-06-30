package kr.mybrary.bookservice.booksearch.presentation.response;

import java.util.List;
import kr.mybrary.bookservice.booksearch.domain.dto.BookSearchResultDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookSearchResultResponse {

    private List<BookSearchResultDto> bookSearchResult;
    private String nextRequestUrl;

    public static BookSearchResultResponse of(List<BookSearchResultDto> bookSearchResultDtos, String nextRequestUrl) {
        return BookSearchResultResponse.builder()
                .bookSearchResult(bookSearchResultDtos)
                .nextRequestUrl(nextRequestUrl)
                .build();
    }
}
