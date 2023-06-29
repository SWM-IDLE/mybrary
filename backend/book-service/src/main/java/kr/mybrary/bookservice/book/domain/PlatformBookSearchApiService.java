package kr.mybrary.bookservice.book.domain;

import java.util.List;
import kr.mybrary.bookservice.book.presentation.dto.response.BookSearchResultResponse;

public interface PlatformBookSearchApiService {

    List<BookSearchResultResponse> searchWithKeyword(String keyword);

    List<BookSearchResultResponse> searchWithISBN(String isbn);
}