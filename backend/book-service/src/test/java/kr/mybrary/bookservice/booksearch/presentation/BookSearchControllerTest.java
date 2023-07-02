package kr.mybrary.bookservice.booksearch.presentation;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.List;
import kr.mybrary.bookservice.booksearch.domain.KakaoBookSearchApiService;
import kr.mybrary.bookservice.booksearch.domain.dto.BookSearchResultDto;
import kr.mybrary.bookservice.booksearch.domain.exception.BookSearchResultNotFoundException;
import kr.mybrary.bookservice.booksearch.presentation.response.BookSearchResultResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookSearchController.class)
@MockBean(JpaMetamodelMappingContext.class)
class BookSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KakaoBookSearchApiService kakaoBookSearchApiService;

    @DisplayName("ISBN 바코드 인식시, ISBN을 통해 책을 검색한다.")
    @Test
    void searchWithISBNBarcodeScan() throws Exception {

        // given
        BookSearchResultResponse response = BookSearchResultResponse.builder()
                .bookSearchResult(List.of(createBookSearchDto()))
                .nextRequestUrl("")
                .build();

        given(kakaoBookSearchApiService.searchWithISBN("9788980782970")).willReturn(response);

        // when, then
        mockMvc.perform(get("/books/search/isbn")
                        .param("isbn", "9788980782970"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("ISBN 검색에 성공했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.bookSearchResult[0].title").value("자바의 정석"))
                .andExpect(jsonPath("$.data.bookSearchResult[0].description").value("자바의 정석 3판"))
                .andExpect(jsonPath("$.data.bookSearchResult[0].authors[0]").value("남궁성"))
                .andExpect(jsonPath("$.data.bookSearchResult[0].isbn13").value("9788980782970"))
                .andExpect(jsonPath("$.data.nextRequestUrl").isEmpty());
    }

    @DisplayName("검색 키워드를 통해 책을 검색한다.")
    @Test
    void searchWithKeyword() throws Exception {

        // given
        BookSearchResultResponse response = BookSearchResultResponse.builder()
                .bookSearchResult(List.of(createBookSearchDto()))
                .nextRequestUrl("/books/search?keyword=자바&sort=accuracy&page=2")
                .build();

        given(kakaoBookSearchApiService.searchWithKeyword("자바", "accuracy", 1))
                .willReturn(response);

        // when, then
        mockMvc.perform(get("/books/search")
                        .param("keyword", "자바")
                        .param("sort", "accuracy")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("키워드 검색에 성공했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.bookSearchResult[0].title").value("자바의 정석"))
                .andExpect(jsonPath("$.data.bookSearchResult[0].description").value("자바의 정석 3판"))
                .andExpect(jsonPath("$.data.bookSearchResult[0].authors[0]").value("남궁성"))
                .andExpect(jsonPath("$.data.bookSearchResult[0].isbn13").value("9788980782970"))
                .andExpect(jsonPath("$.data.nextRequestUrl").value(
                        "/books/search?keyword=자바&sort=accuracy&page=2"));
    }

    @DisplayName("검색어로 존재하지 않는 도서를 검색 시, 예외가 발생한다.")
    @Test
    void searchWithNotExistedKeyword() throws Exception {

        // given
        given(kakaoBookSearchApiService.searchWithKeyword("존재하지않을거야이검색어는", "accuracy",
                1)).willThrow(new BookSearchResultNotFoundException());

        // when, then
        mockMvc.perform(get("/books/search")
                        .param("keyword", "존재하지않을거야이검색어는")
                        .param("sort", "accuracy")
                        .param("page", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("B-01"))
                .andExpect(jsonPath("$.errorMessage").value("도서 검색 결과가 존재하지 않습니다."));
    }

    private BookSearchResultDto createBookSearchDto() {
        return BookSearchResultDto.builder()
                .title("자바의 정석")
                .detailsUrl("자바의 정석 detail Url")
                .description("자바의 정석 3판")
                .isbn10("8980782970")
                .isbn13("9788980782970")
                .authors(List.of("남궁성"))
                .price(25000)
                .thumbnailUrl("https://bookthumb-phinf.pstatic.net/cover/150/077/15007773.jpg?type=m1&udate=20180726")
                .status("정상판매")
                .starRating(0.0)
                .publicationDate(OffsetDateTime.parse("2008-08-01T00:00:00+09:00"))
                .build();
    }
}