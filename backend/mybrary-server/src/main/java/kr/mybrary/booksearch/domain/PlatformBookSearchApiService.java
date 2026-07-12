package kr.mybrary.booksearch.domain;

import kr.mybrary.booksearch.domain.dto.request.BookListByCategorySearchServiceRequest;
import kr.mybrary.booksearch.domain.dto.request.BookSearchServiceRequest;
import kr.mybrary.booksearch.presentation.dto.response.BookListByCategorySearchResultResponse;
import kr.mybrary.booksearch.presentation.dto.response.BookSearchDetailResponse;
import kr.mybrary.booksearch.presentation.dto.response.BookSearchResultResponse;

public interface PlatformBookSearchApiService {

    BookSearchResultResponse searchWithKeyword(BookSearchServiceRequest request);

    BookSearchDetailResponse searchBookDetailWithISBN(BookSearchServiceRequest request);

    BookListByCategorySearchResultResponse searchBookListByCategory(BookListByCategorySearchServiceRequest request);
}
