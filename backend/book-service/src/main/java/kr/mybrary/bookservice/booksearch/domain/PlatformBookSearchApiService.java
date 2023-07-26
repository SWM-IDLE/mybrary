package kr.mybrary.bookservice.booksearch.domain;

public interface PlatformBookSearchApiService {

    Object searchWithKeyword(Object request);

    Object searchWithISBN(Object request);
}
