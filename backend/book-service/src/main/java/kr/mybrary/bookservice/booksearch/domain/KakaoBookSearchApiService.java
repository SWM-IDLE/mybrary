package kr.mybrary.bookservice.booksearch.domain;

import java.util.List;
import java.util.Objects;
import kr.mybrary.bookservice.booksearch.domain.dto.BookSearchDtoMapper;
import kr.mybrary.bookservice.booksearch.domain.dto.BookSearchResultDto;
import kr.mybrary.bookservice.booksearch.domain.dto.response.kakaoapi.Document;
import kr.mybrary.bookservice.booksearch.domain.dto.response.kakaoapi.KakaoBookSearchResponse;
import kr.mybrary.bookservice.booksearch.domain.exception.BookSearchResultNotFoundException;
import kr.mybrary.bookservice.booksearch.domain.dto.request.KakaoServiceRequest;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.KakaoBookSearchResultResponse;
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
public class KakaoBookSearchApiService implements PlatformBookSearchApiService {


    @Value("${kakao.api.key}")
    private String API_KEY;

    private final RestTemplate restTemplate;

    public KakaoBookSearchApiService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    private static final String API_URL_WITH_KEYWORD = "https://dapi.kakao.com/v3/search/book?query=%s&sort=%s&page=%d";
    private static final String API_URL_WITH_ISBN = "https://dapi.kakao.com/v3/search/book?target=isbn&query=%s&sort=%s&page=%d";
    private static final String REQUEST_NEXT_URL = "/books/search?keyword=%s&sort=%s&page=%d";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String KAKAO_AUTHORIZATION_HEADER_PREFIX = "KakaoAK ";

    @Override
    public KakaoBookSearchResultResponse searchWithKeyword(Object serviceRequest) {
        KakaoServiceRequest request = (KakaoServiceRequest) serviceRequest;

        return searchBookFromKakaoApi(API_URL_WITH_KEYWORD, request);
    }

    @Override
    public KakaoBookSearchResultResponse searchWithISBN(Object serviceRequest) {
        KakaoServiceRequest request = (KakaoServiceRequest) serviceRequest;

        return searchBookFromKakaoApi(API_URL_WITH_ISBN, request);
    }

    private KakaoBookSearchResultResponse searchBookFromKakaoApi(String baseUrl, KakaoServiceRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER, KAKAO_AUTHORIZATION_HEADER_PREFIX + API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(headers);

        String requestUri = String.format(baseUrl, request.getKeyword(), request.getSort(), request.getPage());
        ResponseEntity<KakaoBookSearchResponse> response = restTemplate.exchange(requestUri,
                HttpMethod.GET, httpEntity, KakaoBookSearchResponse.class);

        List<Document> documents = Objects.requireNonNull(response.getBody()).getDocuments();

        if (documents.isEmpty()) {
            throw new BookSearchResultNotFoundException();
        }

        List<BookSearchResultDto> bookSearchResultDtos = documents.stream()
                .map(BookSearchDtoMapper.INSTANCE::kakaoSearchResponseToDto)
                .toList();

        if (isLastPage(response)) {
            return KakaoBookSearchResultResponse.of(bookSearchResultDtos, "");
        }

        String nextRequestUrl = String.format(REQUEST_NEXT_URL, request.getKeyword(), request.getSort(), request.getPage() + 1);
        return KakaoBookSearchResultResponse.of(bookSearchResultDtos, nextRequestUrl);
    }

    private Boolean isLastPage(ResponseEntity<KakaoBookSearchResponse> response) {
        return Objects.requireNonNull(response.getBody()).getMeta().getIs_end();
    }
}
