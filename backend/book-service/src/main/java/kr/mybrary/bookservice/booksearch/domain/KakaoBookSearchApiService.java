package kr.mybrary.bookservice.booksearch.domain;

import java.util.List;
import java.util.Objects;
import kr.mybrary.bookservice.booksearch.domain.dto.BookSearchDtoMapper;
import kr.mybrary.bookservice.booksearch.domain.dto.request.BookListByCategorySearchServiceRequest;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchResultResponseElement;
import kr.mybrary.bookservice.booksearch.domain.dto.response.kakaoapi.KakaoBookSearchResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.kakaoapi.KakaoBookSearchResponse.Meta;
import kr.mybrary.bookservice.booksearch.domain.exception.BookSearchResultNotFoundException;
import kr.mybrary.bookservice.booksearch.domain.dto.request.BookSearchServiceRequest;
import kr.mybrary.bookservice.booksearch.domain.exception.UnsupportedSearchAPIException;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookListByCategorySearchResultResponse;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchResultResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Qualifier("kakao")
public class KakaoBookSearchApiService implements PlatformBookSearchApiService {


    @Value("${kakao.api.key}")
    private String API_KEY;

    private static final String API_URL_WITH_KEYWORD = "https://dapi.kakao.com/v3/search/book?query=%s&sort=%s&page=%d";
    private static final String API_URL_WITH_ISBN = "https://dapi.kakao.com/v3/search/book?target=isbn&query=%s&sort=%s&page=%d";
    private static final String REQUEST_NEXT_URL = "/books/search?keyword=%s&sort=%s&page=%d";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String KAKAO_AUTHORIZATION_HEADER_PREFIX = "KakaoAK ";

    private final RestTemplate restTemplate;
    private final BookSearchRankingService bookSearchRankingService;

    public KakaoBookSearchApiService(
            RestTemplateBuilder restTemplateBuilder,
            BookSearchRankingService bookSearchRankingService) {

        this.restTemplate = restTemplateBuilder.build();
        this.bookSearchRankingService = bookSearchRankingService;
    }

    @Override
    public BookSearchResultResponse searchWithKeyword(BookSearchServiceRequest request) {

        KakaoBookSearchResponse searchResponse = requestBookSearch(API_URL_WITH_KEYWORD, request);

        if (searchResponse.getDocuments().isEmpty()) {
            return BookSearchResultResponse.of(List.of(), "");
        }

        List<BookSearchResultResponseElement> response =
                searchResponse.getDocuments().stream()
                .map(BookSearchDtoMapper.INSTANCE::kakaoSearchResponseToServiceResponse)
                .toList();

        bookSearchRankingService.increaseSearchRankingScore(request.getKeyword());

        if (isLastPage(searchResponse.getMeta())) {
            return BookSearchResultResponse.of(response, "");
        }

        String nextRequestUrl = String.format(REQUEST_NEXT_URL, request.getKeyword(), request.getSort(), request.getPage() + 1);
        return BookSearchResultResponse.of(response, nextRequestUrl);
    }

    @Override
    public BookSearchDetailResponse searchBookDetailWithISBN(BookSearchServiceRequest request) {

        KakaoBookSearchResponse response = requestBookSearch(API_URL_WITH_ISBN, request);

        if (response.getDocuments().isEmpty()) {
            throw new BookSearchResultNotFoundException();
        }

        return BookSearchDtoMapper.INSTANCE.kakaoSearchResponseToDetailResponse(response.getDocuments().get(0));
    }

    @Override
    public BookListByCategorySearchResultResponse searchBookListByCategory(BookListByCategorySearchServiceRequest request) {
        throw new UnsupportedSearchAPIException();
    }

    private KakaoBookSearchResponse requestBookSearch(String baseUrl, BookSearchServiceRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER, KAKAO_AUTHORIZATION_HEADER_PREFIX + API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(headers);

        String requestUri = String.format(baseUrl, request.getKeyword(), request.getSort(), request.getPage());
        ResponseEntity<KakaoBookSearchResponse> response = restTemplate.exchange(requestUri,
                HttpMethod.GET, httpEntity, KakaoBookSearchResponse.class);

        List<KakaoBookSearchResponse.Document> documents = Objects.requireNonNull(response.getBody()).getDocuments();

        if (documents.isEmpty()) {
            return emptyResponse();
        }

        return response.getBody();
    }

    private Boolean isLastPage(Meta metaData) {
        return metaData.getIs_end();
    }

    private KakaoBookSearchResponse emptyResponse() {
        return KakaoBookSearchResponse.builder()
                .documents(List.of())
                .meta(Meta.builder()
                        .is_end(true)
                        .pageable_count(0)
                        .total_count(0)
                        .build())
                .build();
    }
}
