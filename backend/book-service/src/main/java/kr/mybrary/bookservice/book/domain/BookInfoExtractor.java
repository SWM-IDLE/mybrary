package kr.mybrary.bookservice.book.domain;

import java.util.List;
import kr.mybrary.bookservice.book.domain.dto.BookInfo;
import kr.mybrary.bookservice.book.infrastructure.booksearch.PlatformBookSearchApiRequester;
import kr.mybrary.bookservice.book.infrastructure.dto.kakaoapi.KakaoBookSearchResponse;
import kr.mybrary.bookservice.book.infrastructure.dto.kakaoapi.Document;
import kr.mybrary.bookservice.exception.book.BookNotFoundWithWrongInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookInfoExtractor {

    private final PlatformBookSearchApiRequester platformBookSearchApiRequester;

    // TODO: 카카오 API에 경우 대해서만 처리되어, OCP 위반 상태 - 좀더 고민해봐야 할 것 같다.
    public List<BookInfo> extractBooksByKeyword(String keyword) {
        KakaoBookSearchResponse kakaoBookSearchResponse = (KakaoBookSearchResponse) platformBookSearchApiRequester.searchWithKeyWord(
                keyword);

        if (kakaoBookSearchResponse.getDocuments().isEmpty()) {
            throw new BookNotFoundWithWrongInputException();
        }

        return kakaoBookSearchResponse.getDocuments().stream()
                .map(this::convertDocumentToBookInfo)
                .toList();
    }

    public BookInfo extractBookByISBN(String isbn) {
        KakaoBookSearchResponse kakaoBookSearchResponse = (KakaoBookSearchResponse) platformBookSearchApiRequester.searchWithISBN(
                isbn);

        if (kakaoBookSearchResponse.getDocuments().isEmpty()) {
            throw new BookNotFoundWithWrongInputException();
        }

        Document document = kakaoBookSearchResponse.getDocuments().get(0);
        return convertDocumentToBookInfo(document);
    }

    private BookInfo convertDocumentToBookInfo(Document document) {
        // TODO: mapSturct 를 통해 매핑
        // TODO: isbn10, 13 분리

        return BookInfo.builder()
                .title(document.getTitle())
                .contents(document.getContents())
                .isbn10(document.getIsbn())
                .isbn13(document.getIsbn())
                .publishDate(document.getDatetime())
                .authors(document.getAuthors())
                .translators(document.getTranslators())
                .price(document.getPrice())
                .thumbnailUrl(document.getThumbnail())
                .build();
    }
}
