package kr.mybrary.bookservice.booksearch.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
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
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchResultResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
@WebMvcTest(BookSearchController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class BookSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KakaoBookSearchApiService kakaoBookSearchApiService;

    @DisplayName("ISBN을 통해 도서의 상세 정보를 조회한다.")
    @Test
    void searchWithISBNBarcodeScan() throws Exception {

        // given
        BookSearchDetailResponse response = BookSearchDtoTestData.createBookSearchDetailResponse();

        given(kakaoBookSearchApiService.searchBookDetailWithISBN(any())).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/books/search/detail")
                .param("isbn", "9788932917245"));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("도서 상세 조회에 성공했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.title").value(response.getTitle()))
                .andExpect(jsonPath("$.data.subTitle").value(response.getSubTitle()))
                .andExpect(jsonPath("$.data.author").value(response.getAuthor()))
                .andExpect(jsonPath("$.data.thumbnail").value(response.getThumbnail()))
                .andExpect(jsonPath("$.data.link").value(response.getLink()))
                .andExpect(jsonPath("$.data.authors[0].name").value(response.getAuthors().get(0).getName()))
                .andExpect(jsonPath("$.data.authors[0].authorId").value(response.getAuthors().get(0).getAuthorId()))
                .andExpect(jsonPath("$.data.translators[0].name").value(response.getTranslators().get(0).getName()))
                .andExpect(jsonPath("$.data.translators[0].translatorId").value(response.getTranslators().get(0).getTranslatorId()))
                .andExpect(jsonPath("$.data.starRating").value(response.getStarRating()))
                .andExpect(jsonPath("$.data.reviewCount").value(response.getReviewCount()))
                .andExpect(jsonPath("$.data.publicationDate").value(response.getPublicationDate()))
                .andExpect(jsonPath("$.data.category").value(response.getCategory()))
                .andExpect(jsonPath("$.data.categoryId").value(response.getCategoryId()))
                .andExpect(jsonPath("$.data.pages").value(response.getPages()))
                .andExpect(jsonPath("$.data.publisher").value(response.getPublisher()))
                .andExpect(jsonPath("$.data.description").value(response.getDescription()))
                .andExpect(jsonPath("$.data.toc").value(response.getToc()))
                .andExpect(jsonPath("$.data.isbn10").value(response.getIsbn10()))
                .andExpect(jsonPath("$.data.isbn13").value(response.getIsbn13()))
                .andExpect(jsonPath("$.data.weight").value(response.getWeight()))
                .andExpect(jsonPath("$.data.sizeDepth").value(response.getSizeDepth()))
                .andExpect(jsonPath("$.data.sizeHeight").value(response.getSizeHeight()))
                .andExpect(jsonPath("$.data.sizeWidth").value(response.getSizeWidth()))
                .andExpect(jsonPath("$.data.priceStandard").value(response.getPriceStandard()))
                .andExpect(jsonPath("$.data.priceSales").value(response.getPriceSales()))
                .andExpect(jsonPath("$.data.holderCount").value(response.getHolderCount()))
                .andExpect(jsonPath("$.data.readCount").value(response.getReadCount()))
                .andExpect(jsonPath("$.data.interestCount").value(response.getInterestCount()));

        // document
        actions
                .andDo(document("book-search-detail-with-isbn",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("search")
                                        .summary("ISBN을 통해 도서 상세를 조회한다.")
                                        .queryParameters(
                                                parameterWithName("isbn").type(SimpleType.STRING).description("ISBN")
                                        )
                                        .responseSchema(Schema.schema("book_search_detail_with_isbn_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data.title").type(STRING).description("도서 제목"),
                                                fieldWithPath("data.subTitle").type(STRING).description("도서 부제목"),
                                                fieldWithPath("data.author").type(STRING).description("도서 저자 목록"),
                                                fieldWithPath("data.thumbnail").type(STRING).description("도서 썸네일"),
                                                fieldWithPath("data.link").type(STRING).description("도서 링크"),
                                                fieldWithPath("data.authors[0].name").type(STRING).description("도서 저자 이름"),
                                                fieldWithPath("data.authors[0].authorId").type(NUMBER).description("도서 저자 ID"),
                                                fieldWithPath("data.translators[0].name").type(STRING).description("도서 번역자 이름"),
                                                fieldWithPath("data.translators[0].translatorId").type(NUMBER).description("도서 번역자 ID"),
                                                fieldWithPath("data.starRating").type(NUMBER).description("도서 별점"),
                                                fieldWithPath("data.reviewCount").type(NUMBER).description("도서 리뷰 수"),
                                                fieldWithPath("data.publicationDate").type(STRING).description("도서 출판일"),
                                                fieldWithPath("data.category").type(STRING).description("도서 카테고리"),
                                                fieldWithPath("data.categoryId").type(NUMBER).description("도서 카테고리 ID"),
                                                fieldWithPath("data.pages").type(NUMBER).description("도서 페이지 수"),
                                                fieldWithPath("data.publisher").type(STRING).description("도서 출판사"),
                                                fieldWithPath("data.description").type(STRING).description("도서 소개"),
                                                fieldWithPath("data.toc").type(STRING).description("도서 목차"),
                                                fieldWithPath("data.isbn10").type(STRING).description("도서 ISBN10"),
                                                fieldWithPath("data.isbn13").type(STRING).description("도서 ISBN13"),
                                                fieldWithPath("data.weight").type(NUMBER).description("도서 무게"),
                                                fieldWithPath("data.sizeDepth").type(NUMBER).description("도서 깊이"),
                                                fieldWithPath("data.sizeHeight").type(NUMBER).description("도서 높이"),
                                                fieldWithPath("data.sizeWidth").type(NUMBER).description("도서 너비"),
                                                fieldWithPath("data.priceStandard").type(NUMBER).description("도서 정가"),
                                                fieldWithPath("data.priceSales").type(NUMBER).description("도서 판매가"),
                                                fieldWithPath("data.holderCount").type(NUMBER).description("도서 보류 수"),
                                                fieldWithPath("data.readCount").type(NUMBER).description("도서 완독 수"),
                                                fieldWithPath("data.interestCount").type(NUMBER).description("도서 관심 수")
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

        given(kakaoBookSearchApiService.searchWithKeyword(any())).willReturn(response);

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
                .andExpect(jsonPath("$.data.bookSearchResult[0].author").value("남궁성"))
                .andExpect(jsonPath("$.data.bookSearchResult[0].isbn13").value("9788980782970"))
                .andExpect(jsonPath("$.data.bookSearchResult[0].thumbnailUrl").value("https://bookthumb-phinf.pstatic.net/cover/150/077/15007773.jpg?type=m1&udate=20180726"))
                .andExpect(jsonPath("$.data.bookSearchResult[0].starRating").value("0.0"))
                .andExpect(jsonPath("$.data.bookSearchResult[0].publicationDate").value("2008-08-01"))

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
                                        .description(("예시로는 검색된 도서가 1권이지만, 실제 검색된 도서가 50권이면 nextRequestUrl에 다음 페이지를 요청할 수 있는 URL이 반환된다."
                                                + " 만일 50권 이하로 검색된다면 nextRequestUrl은 빈 문자열이다."))
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
                                                fieldWithPath("data.bookSearchResult[].isbn13").type(STRING).description("도서 ISBN13"),
                                                fieldWithPath("data.bookSearchResult[].author").type(STRING).description("도서 저자"),
                                                fieldWithPath("data.bookSearchResult[].thumbnailUrl").type(STRING).description("도서 썸네일 URL"),
                                                fieldWithPath("data.bookSearchResult[].starRating").type(NUMBER).description("별점"),
                                                fieldWithPath("data.bookSearchResult[].publicationDate").type(STRING).description("출판일"),
                                                fieldWithPath("data.nextRequestUrl").type(STRING).description("다음 요청 URL")
                                        ).build())));
    }
}