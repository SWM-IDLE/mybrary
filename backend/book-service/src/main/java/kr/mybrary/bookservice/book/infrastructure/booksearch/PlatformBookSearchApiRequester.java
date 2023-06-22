package kr.mybrary.bookservice.book.infrastructure.booksearch;

public interface PlatformBookSearchApiRequester {

    Object searchWithKeyWord(String keyword);

    Object searchWithISBN(String isbn);
}
