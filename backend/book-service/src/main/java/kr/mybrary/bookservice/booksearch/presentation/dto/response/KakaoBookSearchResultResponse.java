package kr.mybrary.bookservice.booksearch.presentation.dto.response;

import java.util.List;
import kr.mybrary.bookservice.booksearch.domain.dto.BookSearchResultDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoBookSearchResultResponse {

    private List<BookSearchResultDto> bookSearchResult;
    private String nextRequestUrl;

    public static KakaoBookSearchResultResponse of(List<BookSearchResultDto> bookSearchResultDtos, String nextRequestUrl) {
        return KakaoBookSearchResultResponse.builder()
                .bookSearchResult(bookSearchResultDtos)
                .nextRequestUrl(nextRequestUrl)
                .build();
    }
}
