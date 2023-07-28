package kr.mybrary.bookservice.booksearch.domain;

import java.util.List;
import java.util.Objects;
import kr.mybrary.bookservice.booksearch.domain.dto.response.BookSearchResultServiceResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.BookSearchDtoMapper;
import kr.mybrary.bookservice.booksearch.domain.dto.request.BookSearchServiceRequest;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookSearchDetailResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookSearchResponse;
import kr.mybrary.bookservice.booksearch.domain.exception.BookSearchResultNotFoundException;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
    private static final String REQUEST_MAX_RESULTS = "20";
    private static final String REQUEST_COVER_SIZE = "Big";

    private final RestTemplate restTemplate;

    public AladinBookSearchApiService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    private static final String ITEM_SEARCH_URL = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
    private static final String ITEM_DETAIL_SEARCH_URL = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx";
    private static final String REQUEST_NEXT_URL = "/books/search?keyword=%s&page=%d&sort=%s";

    @Override
    public BookSearchResultResponse searchWithKeyword(BookSearchServiceRequest request) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(ITEM_SEARCH_URL)
                .queryParam("TTBKey", API_KEY)
                .queryParam("Output", REQUEST_OUTPUT)
                .queryParam("Version", REQUEST_VERSION)
                .queryParam("Query", request.getKeyword())
                .queryParam("Start", String.valueOf(request.getPage()))
                .queryParam("MaxResults", REQUEST_MAX_RESULTS)
                .queryParam("Sort", request.getSort());

        ResponseEntity<AladinBookSearchResponse> response = restTemplate.exchange(
                uriBuilder.build(false).toUriString(),
                HttpMethod.GET,
                null,
                AladinBookSearchResponse.class);

        AladinBookSearchResponse aladinBookSearchResponse = Objects.requireNonNull(response.getBody());

        if (aladinBookSearchResponse.getTotalResults() == 0) {
            throw new BookSearchResultNotFoundException();
        }

        List<BookSearchResultServiceResponse> bookSearchResultServiceResponses = aladinBookSearchResponse.getItem().stream()
                .map(BookSearchDtoMapper.INSTANCE::aladinSearchResponseToServiceResponse)
                .toList();

        if (isLastPage(aladinBookSearchResponse)) {
            return BookSearchResultResponse.of(bookSearchResultServiceResponses, "");
        }

        return BookSearchResultResponse.of(bookSearchResultServiceResponses, getNextRequestUrl(request));
    }

    @Override
    public BookSearchDetailResponse searchBookDetailWithISBN(BookSearchServiceRequest request) {
        return null;
    }

    private String getNextRequestUrl(BookSearchServiceRequest request) {
        return String.format(REQUEST_NEXT_URL, request.getKeyword(), request.getPage() + 1, request.getSort());
    }

    private boolean isLastPage(AladinBookSearchResponse response) {
        int searchBookCount = response.getStartIndex() * response.getItemsPerPage();
        return searchBookCount >= 200 || searchBookCount > response.getTotalResults();
    }
}
