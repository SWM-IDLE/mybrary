package kr.mybrary.bookservice.booksearch.presentation.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookListByCategorySearchResultResponse {

    private List<BookListByCategoryResponseElement> books;
    private String nextRequestUrl;

    public static BookListByCategorySearchResultResponse of(List<BookListByCategoryResponseElement> bookListByCategorySearchResultElement, String nextRequestUrl) {
        return BookListByCategorySearchResultResponse.builder()
                .books(bookListByCategorySearchResultElement)
                .nextRequestUrl(nextRequestUrl)
                .build();
    }
}
