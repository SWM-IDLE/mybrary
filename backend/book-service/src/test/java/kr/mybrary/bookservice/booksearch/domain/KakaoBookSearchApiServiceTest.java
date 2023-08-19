package kr.mybrary.bookservice.booksearch.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import kr.mybrary.bookservice.booksearch.BookSearchDtoTestData;
import kr.mybrary.bookservice.booksearch.domain.dto.request.BookListByCategorySearchServiceRequest;
import kr.mybrary.bookservice.booksearch.domain.dto.request.BookSearchServiceRequest;
import kr.mybrary.bookservice.booksearch.domain.exception.BookSearchResultNotFoundException;
import kr.mybrary.bookservice.booksearch.domain.exception.UnsupportedSearchAPIException;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchResultResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestClientTest(value = KakaoBookSearchApiService.class)
class KakaoBookSearchApiServiceTest {

    private static final String KAKAO_BOOK_SEARCH_API_URL = "https://dapi.kakao.com/v3/search/book";
    private static final String JSON_FILE_PATH = "src/test/resources/kakaoapi/";
    private static final String EXIST_ISBN = "9788980782970";
    private static final String NOT_EXIST_ISBN = "978898078297011";

    @Autowired
    private KakaoBookSearchApiService kakaoBookSearchApiService;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @MockBean
    private BookSearchRankingService bookSearchRankingService;

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
        BookSearchServiceRequest request = BookSearchServiceRequest.of("docker", "accuracy", 1);

        mockServer
                .expect(requestTo(KAKAO_BOOK_SEARCH_API_URL + "?query=docker&sort=accuracy&page=1"))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        // when
        BookSearchResultResponse kakaoBookSearchResultResponse = kakaoBookSearchApiService.searchWithKeyword(request);

        // then
        assertAll(
                () -> assertThat(kakaoBookSearchResultResponse.getBookSearchResult().size()).isEqualTo(10),
                () -> assertThat(kakaoBookSearchResultResponse.getNextRequestUrl()).isEqualTo(expectNextRequestUrl),
                () -> verify(bookSearchRankingService, times(1)).increaseSearchRankingScore(request.getKeyword())
        );
    }

    @DisplayName("카카오 도서 검색의 결과가 10권 이하이면, expectNextRequestUrl는 빈 값이다.")
    @Test
    void searchFromKakaoApiAndResultLessThan10() throws IOException {

        // given
        String expectNextRequestUrl = "";
        BookSearchServiceRequest request = BookSearchServiceRequest.of("Docker Container", "accuracy", 1);

        String expectResult = readJsonFile("resultLessThan10FromKeyword.json");

        mockServer
                .expect(requestTo(KAKAO_BOOK_SEARCH_API_URL + "?query=Docker Container&sort=accuracy&page=1"))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        // when
        BookSearchResultResponse kakaoBookSearchResultResponse = kakaoBookSearchApiService
                .searchWithKeyword(request);

        // then
        assertAll(
                () -> assertThat(kakaoBookSearchResultResponse.getBookSearchResult().size()).isLessThan(10),
                () -> assertThat(kakaoBookSearchResultResponse.getNextRequestUrl()).isEqualTo(expectNextRequestUrl),
                () -> verify(bookSearchRankingService, times(1)).increaseSearchRankingScore(request.getKeyword())
        );
    }

    @DisplayName("ISBN을 통한 도서 검색의 결과는 1권이며, expectNextRequestUrl는 빈 값이다.")
    @Test
    void searchFromKakaoApiWithISBN() throws IOException {

        // given
        String expectNextRequestUrl = "";
        String expectResult = readJsonFile("resultFromISBN.json");

        mockServer
                .expect(requestTo(KAKAO_BOOK_SEARCH_API_URL + "/isbn?isbn=" + EXIST_ISBN))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        BookSearchServiceRequest request = BookSearchServiceRequest.of(EXIST_ISBN);

        // when
        BookSearchResultResponse bookSearchResultResponse = kakaoBookSearchApiService.searchWithKeyword(request);

        // then
        assertAll(
                () -> assertThat(bookSearchResultResponse.getBookSearchResult().size()).isEqualTo(1),
                () -> assertThat(bookSearchResultResponse.getNextRequestUrl()).isEqualTo(expectNextRequestUrl)
        );
    }

    @DisplayName("도서 검색 결과가 존재하지 않으면, 빈 응답을 반환한다.")
    @Test
    void searchFromKakaoApiAndResultNotExist() throws IOException {

        // given
        String expectResult = readJsonFile("resultEmpty.json");

        mockServer
                .expect(requestTo(KAKAO_BOOK_SEARCH_API_URL + "?query=empty keywordr&sort=accuracy&page=1"))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        BookSearchServiceRequest request = BookSearchServiceRequest.of("empty keyword", "accuracy", 1);

        // when
        BookSearchResultResponse response = kakaoBookSearchApiService.searchWithKeyword(request);

        // then
        assertAll(
                () -> assertThat(response.getBookSearchResult()).isEmpty(),
                () -> assertThat(response.getNextRequestUrl()).isEqualTo("")
        );
    }

    @DisplayName("도서 상세 보기 조회를 한다.")
    @Test
    void searchBookDetailFromKakaoApi() throws IOException {

        // given
        String expectResult = readJsonFile("resultFromISBN.json");

        mockServer
                .expect(requestTo(KAKAO_BOOK_SEARCH_API_URL + "/isbn?isbn=" + EXIST_ISBN))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        BookSearchServiceRequest request = BookSearchServiceRequest.of(EXIST_ISBN);

        // when
        BookSearchDetailResponse bookSearchDetailResponse = kakaoBookSearchApiService.searchBookDetailWithISBN(request);

        // then
        assertAll(
                () -> assertThat(bookSearchDetailResponse.getTitle()).isEqualTo("스프링5 프로그래밍 입문"),
                () -> assertThat(bookSearchDetailResponse.getIsbn10()).isEqualTo("8980782977"),
                () -> assertThat(bookSearchDetailResponse.getIsbn13()).isEqualTo("9788980782970")
        );
    }

    @DisplayName("지원되지 않는 플랫폼에서 도서 분야별 리스트 조회시 예외가 발생한다.")
    @Test
    void occurExceptionWhenSearchBookListByField() {

        // given
        BookListByCategorySearchServiceRequest request = BookSearchDtoTestData.createBookListSearchServiceRequest();

        // given, when
        assertThatThrownBy(() -> kakaoBookSearchApiService.searchBookListByCategory(request)).isInstanceOf(
                UnsupportedSearchAPIException.class);
    }

    private String readJsonFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH + fileName)));
    }
}