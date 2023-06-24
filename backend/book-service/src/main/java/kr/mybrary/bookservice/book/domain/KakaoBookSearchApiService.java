package kr.mybrary.bookservice.book.domain;

import java.util.List;
import kr.mybrary.bookservice.book.domain.dto.BookDtoMapper;
import kr.mybrary.bookservice.book.domain.dto.kakaoapi.Document;
import kr.mybrary.bookservice.book.domain.dto.kakaoapi.KakaoBookSearchResponse;
import kr.mybrary.bookservice.book.domain.exception.BookSearchResultNotFoundException;
import kr.mybrary.bookservice.book.presentation.dto.response.BookSearchResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoBookSearchApiService implements PlatformBookSearchApiService {

    private final RestTemplate restTemplate;

    @Value("${kakao.api.key}")
    private String API_KEY;

    private static final String API_URL_WITH_KEYWORD = "https://dapi.kakao.com/v3/search/book?query=%s";
    private static final String API_URL_WITH_ISBN = "https://dapi.kakao.com/v3/search/book?target=isbn&query=%s";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String KAKAO_AUTHORIZATION_HEADER_PREFIX = "KakaoAK ";

    @Override
    public List<BookSearchResultResponse> searchWithKeyword(String keyword) {
        return searchBookFromKakaoApi(API_URL_WITH_KEYWORD, keyword);
    }

    @Override
    public List<BookSearchResultResponse> searchWithISBN(String isbn) {
        return searchBookFromKakaoApi(API_URL_WITH_ISBN, isbn);
    }

    private List<BookSearchResultResponse> searchBookFromKakaoApi(String baseUrl, String searchKeyword) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER, KAKAO_AUTHORIZATION_HEADER_PREFIX + API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(headers);

        String requestUri = String.format(baseUrl, searchKeyword);
        ResponseEntity<KakaoBookSearchResponse> response = restTemplate.exchange(requestUri,
                HttpMethod.GET, httpEntity, KakaoBookSearchResponse.class);

        List<Document> documents = response.getBody().getDocuments();

        if (documents.isEmpty()) {
            throw new BookSearchResultNotFoundException();
        }

        return documents.stream()
                .map(BookDtoMapper.INSTANCE::kakaoSearchResponseToResponseDto)
                .toList();
    }
}
