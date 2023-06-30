package kr.mybrary.bookservice.booksearch.domain;

import kr.mybrary.bookservice.booksearch.presentation.response.BookSearchResultResponse;

public interface PlatformBookSearchApiService {

    BookSearchResultResponse searchWithKeyword(String keyword, String sort, int page);

    BookSearchResultResponse searchWithISBN(String isbn);
}
