package kr.mybrary.bookservice.book.domain;

import kr.mybrary.bookservice.book.presentation.dto.response.BookSearchResultResponse;

public interface PlatformBookSearchApiService {

    BookSearchResultResponse searchWithKeyword(String keyword, String sort, int page);

    BookSearchResultResponse searchWithISBN(String isbn);
}
