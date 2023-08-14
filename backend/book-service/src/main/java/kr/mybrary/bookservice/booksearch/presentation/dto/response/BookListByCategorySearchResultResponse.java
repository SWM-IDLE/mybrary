package kr.mybrary.bookservice.booksearch.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookListByCategorySearchResultResponse {

    private String thumbnailUrl;
    private String isbn13;

}
