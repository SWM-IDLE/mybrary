package kr.mybrary.bookservice.booksearch.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import kr.mybrary.bookservice.booksearch.domain.dto.request.BookSearchServiceRequest;
import kr.mybrary.bookservice.booksearch.domain.exception.BookSearchResultNotFoundException;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchResultResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
@RestClientTest(value = AladinBookSearchApiService.class)
class AladinBookSearchApiServiceTest {

    @Value("${aladin.api.key}")
    private String API_KEY;

    private static final String ITEM_SEARCH_URL = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
    private static final String ITEM_DETAIL_SEARCH_URL = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx";
    private static final String JSON_FILE_PATH = "src/test/resources/aladinapi/";
    private static final String EXIST_ISBN = "9788980782970";
    private static final String NOT_EXIST_ISBN = "978898078297011";

    @Autowired
    private AladinBookSearchApiService aladinBookSearchApiService;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setup() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @DisplayName("알라딘 도서 검색의 결과는 최대 20권이며, 20권이 넘어가면 expectNextRequestUrl이 존재한다.")
    @Test
    void searchWithKeywordAndResultMoreThan20() throws IOException {

        // given
        BookSearchServiceRequest request = BookSearchServiceRequest.of("Docker", "accuracy", 1);
        String expectResult = readJsonFile("resultMoreThan20FromKeyword.json");
        String expectNextRequestUrl = "/books/search?keyword=Docker&page=2&sort=accuracy";

        mockServer
                .expect(requestTo(ITEM_SEARCH_URL
                        + "?query=Docker&MaxResults=20&start=1&output=js&Version=20131101&Sort=accuracy&TTBKey="
                        + API_KEY))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        // when
        BookSearchResultResponse bookSearchResultResponse = aladinBookSearchApiService.searchWithKeyword(request);

        // then
        assertAll(
                () -> assertThat(bookSearchResultResponse.getBookSearchResult().size()).isEqualTo(20),
                () -> assertThat(bookSearchResultResponse.getNextRequestUrl()).isEqualTo(expectNextRequestUrl)
        );
    }

    @DisplayName("알라딘 도서 검색의 결과가 20권 이하이면, expectNextRequestUrl는 빈 값이다.")
    @Test
    void searchWithKeywordAndResultLessThan20() throws IOException {

        // given
        BookSearchServiceRequest request = BookSearchServiceRequest.of("Docker", "accuracy", 2);
        String expectResult = readJsonFile("resultLessThan20FromKeyword.json");
        String expectNextRequestUrl = "";

        mockServer
                .expect(requestTo(ITEM_SEARCH_URL
                        + "?query=Docker&MaxResults=20&start=2&output=js&Version=20131101&Sort=accuracy&TTBKey="
                        + API_KEY))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        // when
        BookSearchResultResponse bookSearchResultResponse = aladinBookSearchApiService.searchWithKeyword(request);

        // then
        assertAll(
                () -> assertThat(bookSearchResultResponse.getBookSearchResult().size()).isLessThan(20),
                () -> assertThat(bookSearchResultResponse.getNextRequestUrl()).isEqualTo(expectNextRequestUrl)
        );
    }

    @DisplayName("알라딘의 최대 도서 검색 권수는 200권이다. 200권 이상 검색시 expectNextRequestUrl은 빈 값이다.")
    @Test
    void searchWithKeywordAndLimit200() throws IOException {

        // given
        BookSearchServiceRequest request = BookSearchServiceRequest.of("알라", "accuracy", 10);
        String expectResult = readJsonFile("resultOverLimit200FromKeyword.json");
        String expectNextRequestUrl = "";

        mockServer
                .expect(requestTo(ITEM_SEARCH_URL
                        + "?query=알라&MaxResults=20&start=10&output=js&Version=20131101&Sort=accuracy&TTBKey="
                        + API_KEY))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        // when
        BookSearchResultResponse bookSearchResultResponse = aladinBookSearchApiService.searchWithKeyword(request);

        // then
        assertAll(
                () -> assertThat(bookSearchResultResponse.getBookSearchResult().size()).isEqualTo(20),
                () -> assertThat(bookSearchResultResponse.getNextRequestUrl()).isEqualTo(expectNextRequestUrl)
        );
    }

    @DisplayName("도서 검색 결과가 없으면 예외가 발생한다.")
    @Test
    void searchWithKeywordAndResultEmpty() throws IOException {

        // given
        BookSearchServiceRequest request = BookSearchServiceRequest.of("JPA알라", "accuracy", 1);
        String expectResult = readJsonFile("resultEmptyFromKeyword.json");

        mockServer
                .expect(requestTo(ITEM_SEARCH_URL
                        + "?query=JPA알라&MaxResults=20&start=1&output=js&Version=20131101&Sort=accuracy&TTBKey="
                        + API_KEY))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        // when, then
        assertThatThrownBy(() -> aladinBookSearchApiService.searchWithKeyword(request))
                .isInstanceOf(BookSearchResultNotFoundException.class);
    }

    private String readJsonFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH + fileName)));
    }
}