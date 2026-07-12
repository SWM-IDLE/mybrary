package kr.mybrary.book.domain;

import java.util.List;
import kr.mybrary.book.domain.dto.response.BookRecommendationsServiceResponse;
import kr.mybrary.book.domain.dto.response.BookRecommendationsServiceResponse.BookRecommendationsResponseElement;
import kr.mybrary.booksearch.domain.PlatformBookSearchApiService;
import kr.mybrary.booksearch.domain.dto.request.BookListByCategorySearchServiceRequest;
import kr.mybrary.booksearch.presentation.dto.response.BookListByCategorySearchResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * BookServicePort 실제 구현체.
 * 모놀리식 환경에서 PlatformBookSearchApiService를 직접 주입받아 호출한다.
 * user-service의 BookServiceClient(Feign) → book-service GET /api/v1/books/recommendations/...
 * 경로를 내부 메서드 호출로 대체한다.
 */
@Component
@RequiredArgsConstructor
public class BookServicePortImpl implements BookServicePort {

    private final PlatformBookSearchApiService platformBookSearchApiService;

    @Override
    public BookRecommendationsServiceResponse getBookListByCategoryId(String type, int categoryId, int page) {
        BookListByCategorySearchResultResponse result = platformBookSearchApiService.searchBookListByCategory(
                BookListByCategorySearchServiceRequest.of(type, categoryId, page));

        List<BookRecommendationsResponseElement> books = result.getBooks().stream()
                .map(element -> BookRecommendationsResponseElement.builder()
                        .thumbnailUrl(element.getThumbnailUrl())
                        .isbn13(element.getIsbn13())
                        .build())
                .toList();

        return BookRecommendationsServiceResponse.builder()
                .data(BookRecommendationsServiceResponse.Data.builder()
                        .books(books)
                        .build())
                .build();
    }
}
