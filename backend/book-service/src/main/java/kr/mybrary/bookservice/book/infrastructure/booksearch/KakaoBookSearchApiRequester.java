package kr.mybrary.bookservice.book.infrastructure.booksearch;

import kr.mybrary.bookservice.book.infrastructure.dto.kakaoapi.KakaoBookSearchResponse;
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
public class KakaoBookSearchApiRequester implements PlatformBookSearchApiRequester {

    private final RestTemplate restTemplate;

    @Value("${kakao.api.key}")
    private String API_KEY;

    private final String API_URL_WITH_KEYWORD = "https://dapi.kakao.com/v3/search/book?query=%s";
    private final String API_URL_WITH_ISBN = "https://dapi.kakao.com/v3/search/book?target=isbn&query=%s";

    @Override
    public KakaoBookSearchResponse searchWithKeyWord(String keyword) {

        HttpEntity<HttpHeaders> httpEntity = createHttpEntity();

        String requestUri = String.format(API_URL_WITH_KEYWORD, keyword);
        ResponseEntity<KakaoBookSearchResponse> exchange = restTemplate.exchange(requestUri,
                HttpMethod.GET, httpEntity,
                KakaoBookSearchResponse.class);

        return exchange.getBody();
    }

    @Override
    public KakaoBookSearchResponse searchWithISBN(String isbn) {

        HttpEntity<HttpHeaders> httpEntity = createHttpEntity();

        String requestUri = String.format(API_URL_WITH_ISBN, isbn);
        ResponseEntity<KakaoBookSearchResponse> exchange = restTemplate.exchange(requestUri,
                HttpMethod.GET, httpEntity,
                KakaoBookSearchResponse.class);

        return exchange.getBody();
    }

    private HttpEntity<HttpHeaders> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(headers);
        return httpEntity;
    }
}
