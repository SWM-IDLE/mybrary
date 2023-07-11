package kr.mybrary.bookservice.booksearch.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import java.util.List;
import kr.mybrary.bookservice.booksearch.BookSearchDtoTestData;
import kr.mybrary.bookservice.booksearch.domain.KakaoBookSearchApiService;
import kr.mybrary.bookservice.booksearch.presentation.response.BookSearchResultResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(BookSearchController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
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
                .bookSearchResult(List.of(BookSearchDtoTestData.createBookSearchDto()))
                .nextRequestUrl("")
                .build();

        given(kakaoBookSearchApiService.searchWithISBN("9788980782970")).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/books/search/isbn")
                .param("isbn", "9788980782970"));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("ISBN 검색에 성공했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.bookSearchResult[0].title").value("자바의 정석"))
                .andExpect(jsonPath("$.data.bookSearchResult[0].description").value("자바의 정석 3판"))
                .andExpect(jsonPath("$.data.bookSearchResult[0].authors[0]").value("남궁성"))
                .andExpect(jsonPath("$.data.bookSearchResult[0].isbn13").value("9788980782970"))
                .andExpect(jsonPath("$.data.nextRequestUrl").value(""));

        // document
        actions
                .andDo(document("book-search-with-isbn",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("search")
                                        .summary("ISBN으로 도서 검색한다.")
                                        .description("ISBN으로 검색한 결과는 단 한권의 도서이거나 검색 결과가 없을 수 있다."
                                                + " 따라서 항상 nextRequestUrl는 빈 문자열이다.")
                                        .queryParameters(
                                                parameterWithName("isbn").type(SimpleType.STRING).description("ISBN")
                                        )
                                        .responseSchema(Schema.schema("book_search_with_isbn_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data.bookSearchResult").type(ARRAY).description("도서 검색 결과"),
                                                fieldWithPath("data.bookSearchResult[].title").type(STRING).description("도서 제목"),
                                                fieldWithPath("data.bookSearchResult[].description").type(STRING).description("도서 설명"),
                                                fieldWithPath("data.bookSearchResult[].detailsUrl").type(STRING).description("도서 상세 URL"),
                                                fieldWithPath("data.bookSearchResult[].isbn10").type(STRING).description("도서 ISBN10"),
                                                fieldWithPath("data.bookSearchResult[].isbn13").type(STRING).description("도서 ISBN13"),
                                                fieldWithPath("data.bookSearchResult[].authors").type(ARRAY).description("도서 저자"),
                                                fieldWithPath("data.bookSearchResult[].translators").type(ARRAY).description("도서 번역자"),
                                                fieldWithPath("data.bookSearchResult[].price").type(NUMBER).description("도서 가격"),
                                                fieldWithPath("data.bookSearchResult[].salePrice").type(NUMBER).description("도서 판매가"),
                                                fieldWithPath("data.bookSearchResult[].thumbnailUrl").type(STRING).description("도서 썸네일 URL"),
                                                fieldWithPath("data.bookSearchResult[].starRating").type(NUMBER).description("별점"),
                                                fieldWithPath("data.bookSearchResult[].status").type(STRING).description("정상 판매 상태"),
                                                fieldWithPath("data.bookSearchResult[].publicationDate").type(STRING).description("출판일"),
                                                fieldWithPath("data.bookSearchResult[].publisher").type(STRING).description("출판사"),
                                                fieldWithPath("data.nextRequestUrl").type(STRING).description("다음 요청 URL")
                                        ).build())));
    }

    @DisplayName("검색 키워드를 통해 책을 검색한다.")
    @Test
    void searchWithKeyword() throws Exception {

        // given
        BookSearchResultResponse response = BookSearchResultResponse.builder()
                .bookSearchResult(List.of(BookSearchDtoTestData.createBookSearchDto()))
                .nextRequestUrl("/books/search?keyword=자바&sort=accuracy&page=2")
                .build();

        given(kakaoBookSearchApiService.searchWithKeyword("자바", "accuracy", 1))
                .willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/books/search")
                .param("keyword", "자바")
                .param("sort", "accuracy")
                .param("page", "1"));

        // then
        actions
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

        // document
        actions
                .andDo(document("book-search-with-keyword",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("search")
                                        .summary("키워드으로 도서 검색한다. (최대 10권)")
                                        .description(("예시로는 검색된 도서가 1권이지만, 실제 검색된 도서가 10권이면 nextRequestUrl에 다음 페이지를 요청할 수 있는 URL이 반환된다."
                                                + " 만일 10권 이하로 검색된다면 nextRequestUrl은 빈 문자열이다."))
                                        .queryParameters(
                                                parameterWithName("keyword").type(SimpleType.STRING).description("키워드"),
                                                parameterWithName("sort").type(SimpleType.STRING).optional().defaultValue("accuracy").description("정렬 방식"),
                                                parameterWithName("page").type(SimpleType.NUMBER).optional().defaultValue(1).description("페이지 번호")
                                        )
                                        .responseSchema(Schema.schema("book_search_with_keyword_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data.bookSearchResult").type(ARRAY).description("도서 검색 결과"),
                                                fieldWithPath("data.bookSearchResult[].title").type(STRING).description("도서 제목"),
                                                fieldWithPath("data.bookSearchResult[].description").type(STRING).description("도서 설명"),
                                                fieldWithPath("data.bookSearchResult[].detailsUrl").type(STRING).description("도서 상세 URL"),
                                                fieldWithPath("data.bookSearchResult[].isbn10").type(STRING).description("도서 ISBN10"),
                                                fieldWithPath("data.bookSearchResult[].isbn13").type(STRING).description("도서 ISBN13"),
                                                fieldWithPath("data.bookSearchResult[].authors").type(ARRAY).description("도서 저자"),
                                                fieldWithPath("data.bookSearchResult[].translators").type(ARRAY).description("도서 번역자"),
                                                fieldWithPath("data.bookSearchResult[].price").type(NUMBER).description("도서 가격"),
                                                fieldWithPath("data.bookSearchResult[].salePrice").type(NUMBER).description("도서 판매가"),
                                                fieldWithPath("data.bookSearchResult[].thumbnailUrl").type(STRING).description("도서 썸네일 URL"),
                                                fieldWithPath("data.bookSearchResult[].starRating").type(NUMBER).description("별점"),
                                                fieldWithPath("data.bookSearchResult[].status").type(STRING).description("정상 판매 상태"),
                                                fieldWithPath("data.bookSearchResult[].publicationDate").type(STRING).description("출판일"),
                                                fieldWithPath("data.bookSearchResult[].publisher").type(STRING).description("출판사"),
                                                fieldWithPath("data.nextRequestUrl").type(STRING).description("다음 요청 URL")
                                        ).build())));
    }
}