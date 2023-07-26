package kr.mybrary.bookservice.booksearch.domain;

import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchResultResponse;

public interface PlatformBookSearchApiService {

    BookSearchResultResponse searchWithKeyword(Object request);

    BookSearchResultResponse searchWithISBN(Object request);
}
