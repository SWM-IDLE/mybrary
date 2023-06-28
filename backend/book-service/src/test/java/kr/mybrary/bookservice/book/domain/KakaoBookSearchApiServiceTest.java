package kr.mybrary.bookservice.book.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import kr.mybrary.bookservice.book.domain.exception.BookSearchResultNotFoundException;
import kr.mybrary.bookservice.book.presentation.dto.response.BookSearchResultResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@MockBean(JpaMetamodelMappingContext.class)
@RestClientTest(value = KakaoBookSearchApiService.class)
class KakaoBookSearchApiServiceTest {

    private static final String kakaoBookSearchApiUrl = "https://dapi.kakao.com/v3/search/book";
    private static final String jsonFilePath = "src/test/resources/kakaoapi/";

    @Autowired
    private KakaoBookSearchApiService kakaoBookSearchApiService;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setup() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @DisplayName("카카오 도서 검색의 결과는 최대 10권이며, 10권이 넘어가면 expectNextRequestUrl이 존재한다.")
    @Test
    void searchFromKakaoApiAndResultMoreThan10() throws IOException {

        // given
        String expectNextRequestUrl = "/books/search?keyword=docker&sort=accuracy&page=2";

        String expectResult = readJsonFile("resultMoreThan10FromKeyword.json");

        mockServer
                .expect(requestTo(kakaoBookSearchApiUrl + "?query=docker&sort=accuracy&page=1"))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        // when
        BookSearchResultResponse bookSearchResultResponse = kakaoBookSearchApiService
                .searchWithKeyword("docker", "accuracy", 1);

        // then
        assertAll(
                () -> assertThat(bookSearchResultResponse.getBookSearchResult().size()).isEqualTo(10),
                () -> assertThat(bookSearchResultResponse.getNextRequestUrl()).isEqualTo(expectNextRequestUrl)
        );
    }

    @DisplayName("카카오 도서 검색의 결과가 10권 이하이면, expectNextRequestUrl는 빈 값이다.")
    @Test
    void searchFromKakaoApiAndResultLessThan10() throws IOException {

        // given
        String expectNextRequestUrl = "";

        String expectResult = readJsonFile("resultLessThan10FromKeyword.json");

        mockServer
                .expect(requestTo(kakaoBookSearchApiUrl + "?query=Docker Container&sort=accuracy&page=1"))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        // when
        BookSearchResultResponse bookSearchResultResponse = kakaoBookSearchApiService
                .searchWithKeyword("Docker Container", "accuracy", 1);

        // then
        assertAll(
                () -> assertThat(bookSearchResultResponse.getBookSearchResult().size()).isLessThan(10),
                () -> assertThat(bookSearchResultResponse.getNextRequestUrl()).isEqualTo(expectNextRequestUrl)
        );
    }

    @DisplayName("ISBN을 통한 도서 검색의 결과는 1권이며, expectNextRequestUrl는 빈 값이다.")
    @Test
    void searchFromKakaoApiWithISBN() throws IOException {

        // given
        String expectNextRequestUrl = "";

        String expectResult = readJsonFile("resultFromISBN.json");

        mockServer
                .expect(requestTo(kakaoBookSearchApiUrl + "/isbn?isbn=9788980782970"))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        // when
        BookSearchResultResponse bookSearchResultResponse =
                kakaoBookSearchApiService.searchWithISBN("9788980782970");

        // then
        assertAll(
                () -> assertThat(bookSearchResultResponse.getBookSearchResult().size()).isEqualTo(1),
                () -> assertThat(bookSearchResultResponse.getNextRequestUrl()).isEqualTo(expectNextRequestUrl)
        );
    }

    @DisplayName("도서 검색 결과가 존재하지 않으면, 예외가 발생한다.")
    @Test
    void searchFromKakaoApiAndResultNotExist() throws IOException {

        // given
        String expectResult = readJsonFile("resultEmpty.json");

        mockServer
                .expect(requestTo(kakaoBookSearchApiUrl + "/isbn?isbn=978898078297011"))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        // when then
        assertThatThrownBy(
                () -> kakaoBookSearchApiService.searchWithISBN("978898078297011"))
                .isInstanceOf(BookSearchResultNotFoundException.class);
    }

    private String readJsonFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(jsonFilePath + fileName)));
    }
}