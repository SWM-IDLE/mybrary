package kr.mybrary.bookservice.booksearch.domain;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import java.util.Objects;
import kr.mybrary.bookservice.booksearch.domain.dto.BookSearchDtoMapper;
import kr.mybrary.bookservice.booksearch.domain.dto.request.BookListByCategorySearchServiceRequest;
import kr.mybrary.bookservice.booksearch.domain.dto.request.BookSearchServiceRequest;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookListByCategorySearchResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookSearchDetailResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookSearchResponse;
import kr.mybrary.bookservice.booksearch.domain.exception.AladinApiUnavailableException;
import kr.mybrary.bookservice.booksearch.domain.exception.BookSearchResultNotFoundException;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookListByCategoryResponseElement;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookListByCategorySearchResultResponse;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchResultResponse;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchResultResponseElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Qualifier("aladin")
@Primary
@Slf4j
public class AladinBookSearchApiService implements PlatformBookSearchApiService {

    @Value("${aladin.api.key}")
    private String API_KEY;

    private static final String REQUEST_OUTPUT = "js";
    private static final String REQUEST_VERSION = "20131101";
    private static final String REQUEST_MAX_RESULTS_10 = "10";
    private static final String REQUEST_MAX_RESULTS_20 = "20";
    private static final String REQUEST_COVER_SIZE = "Big";
    private static final String REQUEST_COVER_MID_BIG_SIZE = "MidBig";
    private static final String CIRCUIT_BREAKER_CONFIG = "aladinAPICircuitBreakerConfig";
    private static final String RETRY_CONFIG = "aladinAPIRetryConfig";

    private final RestTemplate restTemplate;
    private final BookSearchRankingService bookSearchRankingService;

    public AladinBookSearchApiService(
            RestTemplateBuilder restTemplateBuilder,
            BookSearchRankingService bookSearchRankingService) {

        this.restTemplate = restTemplateBuilder.build();
        this.bookSearchRankingService = bookSearchRankingService;
    }

    private static final String BOOK_SEARCH_URL = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
    private static final String BOOK_DETAIL_SEARCH_URL = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx";
    private static final String BOOK_LIST_BY_CATEGORY_SEARCH_URL = "http://www.aladin.co.kr/ttb/api/ItemList.aspx";
    private static final String BOOK_SEARCH_NEXT_URL = "/books/search?keyword=%s&page=%d&sort=%s";

    @Override
    @Cacheable(cacheNames = "bookListBySearchKeyword", key = "#request.keyword + '_' + #request.sort + '_' + #request.page", cacheManager = "cacheManager")
    @Retry(name = RETRY_CONFIG, fallbackMethod = "searchWithKeywordFallback")
    @CircuitBreaker(name = CIRCUIT_BREAKER_CONFIG, fallbackMethod = "searchWithKeywordFallback")
    public BookSearchResultResponse searchWithKeyword(BookSearchServiceRequest request) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(BOOK_SEARCH_URL)
                .queryParam("TTBKey", API_KEY)
                .queryParam("Output", REQUEST_OUTPUT)
                .queryParam("Version", REQUEST_VERSION)
                .queryParam("Query", request.getKeyword())
                .queryParam("Start", String.valueOf(request.getPage()))
                .queryParam("MaxResults", REQUEST_MAX_RESULTS_20)
                .queryParam("Cover", REQUEST_COVER_MID_BIG_SIZE)
                .queryParam("Sort", request.getSort());

        ResponseEntity<AladinBookSearchResponse> searchResponse = restTemplate.exchange(
                uriBuilder.build(false).toUriString(),
                HttpMethod.GET,
                null,
                AladinBookSearchResponse.class);

        AladinBookSearchResponse response = Objects.requireNonNull(searchResponse.getBody());

        if (response.getTotalResults() == 0) {
            return BookSearchResultResponse.of(List.of(), "");
        }

        List<BookSearchResultResponseElement> bookSearchResultResponseElement = response.getItem().stream()
                .filter(book -> hasISBN13(book.getIsbn13()))
                .map(BookSearchDtoMapper.INSTANCE::aladinSearchResponseToServiceResponse)
                .toList();

        bookSearchRankingService.increaseSearchRankingScore(request.getKeyword());

        if (isLastPage(response.getStartIndex(), response.getItemsPerPage(), response.getTotalResults())) {
            return BookSearchResultResponse.of(bookSearchResultResponseElement, "");
        }

        return BookSearchResultResponse.of(bookSearchResultResponseElement, getNextRequestUrl(request));
    }

    @Override
    @Retry(name = RETRY_CONFIG, fallbackMethod = "searchBookDetailWithISBNFallback")
    @CircuitBreaker(name = CIRCUIT_BREAKER_CONFIG, fallbackMethod = "searchWithKeywordFallback")
    public BookSearchDetailResponse searchBookDetailWithISBN(BookSearchServiceRequest request) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(BOOK_DETAIL_SEARCH_URL)
                .queryParam("TTBKey", API_KEY)
                .queryParam("Output", REQUEST_OUTPUT)
                .queryParam("Version", REQUEST_VERSION)
                .queryParam("ItemId", request.getKeyword())
                .queryParam("Cover", REQUEST_COVER_SIZE)
                .queryParam("ItemIdType", "ISBN13")
                .queryParam("OptResult", "packing,ratingInfo,authors,fulldescription,Toc");

        ResponseEntity<AladinBookSearchDetailResponse> searchResponse = restTemplate.exchange(
                uriBuilder.build(false).toUriString(),
                HttpMethod.GET,
                null,
                AladinBookSearchDetailResponse.class);

        AladinBookSearchDetailResponse response = Objects.requireNonNull(searchResponse.getBody());

        checkIfSearchResultExists(response.getTotalResults());

        return BookSearchDtoMapper.INSTANCE.aladinSearchResponseToDetailResponse(response.getItem().get(0));
    }

    @Override
    @Cacheable(cacheNames = "bookListByCategory", key = "#request.type + '_' + #request.categoryId + '_' + #request.page", cacheManager = "cacheManager")
    @Retry(name = RETRY_CONFIG, fallbackMethod = "searchBookListByCategoryFallback")
    @CircuitBreaker(name = CIRCUIT_BREAKER_CONFIG, fallbackMethod = "searchWithKeywordFallback")
    public BookListByCategorySearchResultResponse searchBookListByCategory(BookListByCategorySearchServiceRequest request) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(BOOK_LIST_BY_CATEGORY_SEARCH_URL)
                .queryParam("TTBKey", API_KEY)
                .queryParam("QueryType", request.getType())
                .queryParam("MaxResults", REQUEST_MAX_RESULTS_10)
                .queryParam("Start", String.valueOf(request.getPage()))
                .queryParam("Output", REQUEST_OUTPUT)
                .queryParam("Version", REQUEST_VERSION)
                .queryParam("CategoryId", String.valueOf(request.getCategoryId()))
                .queryParam("SearchTarget", "BOOK")
                .queryParam("Cover", REQUEST_COVER_SIZE);

        ResponseEntity<AladinBookListByCategorySearchResponse> searchResponse = restTemplate.exchange(
                uriBuilder.build(false).toUriString(),
                HttpMethod.GET,
                null,
                AladinBookListByCategorySearchResponse.class);

        AladinBookListByCategorySearchResponse response = Objects.requireNonNull(searchResponse.getBody());

        checkIfSearchResultExists(response.getTotalResults());

        List<BookListByCategoryResponseElement> bookSearchResultServiceResponses =
                response.getItem().stream()
                .filter(book -> hasISBN13(book.getIsbn13()))
                .map(BookSearchDtoMapper.INSTANCE::aladinBookListByCategorySearchResponseToServiceResponse)
                .toList();

        return BookListByCategorySearchResultResponse.of(bookSearchResultServiceResponses);
    }

    private BookSearchResultResponse searchWithKeywordFallback(BookSearchServiceRequest request, Exception ex) {
        log.info("fallback, the request is searchWithKeyword with '{}' keyword", request.getKeyword());
        log.info("exception message is {}", ex.getMessage());
        throw new AladinApiUnavailableException();
    }

    private BookSearchDetailResponse searchBookDetailWithISBNFallback(BookSearchServiceRequest request, Exception ex) {
        log.info("fallback, the request is searchBookDetailWithISBN with '{}' isbn13", request.getKeyword());
        log.info("exception message is {}", ex.getMessage());
        throw new AladinApiUnavailableException();
    }

    private BookListByCategorySearchResultResponse searchBookListByCategoryFallback(BookListByCategorySearchServiceRequest request, Exception ex) {
        log.info("fallback, the request is searchBookListByCategory with '{}' categoryId", request.getCategoryId());
        log.info("exception message is {}", ex.getMessage());
        throw new AladinApiUnavailableException();
    }

    private void checkIfSearchResultExists(int totalResults) {
        if (totalResults == 0) {
            throw new BookSearchResultNotFoundException();
        }
    }

    private boolean hasISBN13(String isbn13) {
        return !isbn13.isBlank();
    }

    private String getNextRequestUrl(BookSearchServiceRequest request) {
        return String.format(BOOK_SEARCH_NEXT_URL, request.getKeyword(), request.getPage() + 1, request.getSort());
    }

    private boolean isLastPage(int startIndex, int itemsPerPage, int totalResults) {
        int searchBookCount = startIndex * itemsPerPage;
        return searchBookCount >= 200 || searchBookCount > totalResults;
    }
}
