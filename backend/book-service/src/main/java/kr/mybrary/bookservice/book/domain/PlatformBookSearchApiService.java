package kr.mybrary.bookservice.book.domain;

public interface PlatformBookSearchApiService {

    Object searchWithKeyWord(String keyword);

    Object searchWithISBN(String isbn);
}
