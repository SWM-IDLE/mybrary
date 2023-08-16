package kr.mybrary.bookservice.book.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kr.mybrary.bookservice.book.BookDtoTestData;
import kr.mybrary.bookservice.book.domain.BookInterestService;
import kr.mybrary.bookservice.book.domain.BookReadService;
import kr.mybrary.bookservice.book.domain.BookWriteService;
import kr.mybrary.bookservice.book.domain.dto.request.BookDetailServiceRequest;
import kr.mybrary.bookservice.book.presentation.dto.response.BookDetailResponse;
import kr.mybrary.bookservice.book.presentation.dto.request.BookCreateRequest;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestElementResponse;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestHandleResponse;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestStatusResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
@WebMvcTest(BookController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class BookControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookReadService bookReadService;

    @MockBean
    private BookWriteService bookWriteService;

    @MockBean
    private BookInterestService bookInterestService;

    @DisplayName("새로운 도서를 등록한다.")
    @Test
    void create() throws Exception {

        // given
        BookCreateRequest request = BookDtoTestData.createBookCreateRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        doNothing().when(bookWriteService).create(any());

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("201 CREATED"))
                .andExpect(jsonPath("$.message").value("도서 등록에 성공했습니다."))
                .andExpect(jsonPath("$.data").isEmpty());

        // document
        actions
                .andDo(document("book-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("book")
                                        .summary("도서를 등록한다.")
                                        .requestSchema(Schema.schema("book_create_request_body"))
                                        .requestFields(
                                                fieldWithPath("title").type(STRING).description("도서 제목"),
                                                fieldWithPath("subTitle").type(STRING).description("도서 부제목"),
                                                fieldWithPath("author").type(STRING).description("도서 저자 목록"),
                                                fieldWithPath("thumbnailUrl").type(STRING).description("도서 썸네일 URL"),
                                                fieldWithPath("link").type(STRING).description("도서 링크"),
                                                fieldWithPath("translators").type(ARRAY).description("도서 번역가"),
                                                fieldWithPath("translators[].name").type(STRING)
                                                        .description("도서 번역가 이름"),
                                                fieldWithPath("translators[].translatorId").type(NUMBER)
                                                        .description("도서 번역가 ID"),
                                                fieldWithPath("authors").type(ARRAY).description("도서 저자"),
                                                fieldWithPath("authors[].name").type(STRING).description("도서 저자 이름"),
                                                fieldWithPath("authors[].authorId").type(NUMBER)
                                                        .description("도서 저자 ID"),
                                                fieldWithPath("starRating").type(NUMBER).description("도서 별점"),
                                                fieldWithPath("reviewCount").type(NUMBER).description("도서 리뷰 수"),
                                                fieldWithPath("publicationDate").type(STRING).description("출판일"),
                                                fieldWithPath("category").type(STRING).description("카테고리"),
                                                fieldWithPath("categoryId").type(NUMBER).description("카테고리 ID"),
                                                fieldWithPath("pages").type(NUMBER).description("도서 페이지 수"),
                                                fieldWithPath("publisher").type(STRING).description("출판사"),
                                                fieldWithPath("description").type(STRING).description("도서 설명"),
                                                fieldWithPath("toc").type(STRING).description("도서 목차"),
                                                fieldWithPath("isbn10").type(STRING).description("도서 ISBN10"),
                                                fieldWithPath("isbn13").type(STRING).description("도서 ISBN13"),
                                                fieldWithPath("weight").type(NUMBER).description("도서 무게"),
                                                fieldWithPath("sizeDepth").type(NUMBER).description("도서 무게"),
                                                fieldWithPath("sizeHeight").type(NUMBER).description("도서 무게"),
                                                fieldWithPath("sizeWidth").type(NUMBER).description("도서 무게"),
                                                fieldWithPath("priceSales").type(NUMBER).description("도서 판매기"),
                                                fieldWithPath("priceStandard").type(NUMBER).description("도서 정가")
                                        )
                                        .responseSchema(Schema.schema("book_create_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data").type(OBJECT).description("응답 데이터").optional()
                                        )
                                        .build())));
    }

    @DisplayName("도서 상세 정보를 조회한다.")
    @Test
    void getBookDetail() throws Exception {

        // given
        BookDetailResponse response = BookDtoTestData.createBookDetailServiceResponse();

        given(bookReadService.getBookDetailByISBN(any(BookDetailServiceRequest.class)))
                .willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/books/detail")
                .header("USER-ID", "LOGIN_USER_ID")
                .param("isbn10", "9788932917245")
                .param("isbn13", "9788932917245111"));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("도서 상세정보 조회에 성공했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.title").value(response.getTitle()))
                .andExpect(jsonPath("$.data.subTitle").value(response.getSubTitle()))
                .andExpect(jsonPath("$.data.author").value(response.getAuthor()))
                .andExpect(jsonPath("$.data.thumbnail").value(response.getThumbnail()))
                .andExpect(jsonPath("$.data.link").value(response.getLink()))
                .andExpect(jsonPath("$.data.authors[0].name").value(response.getAuthors().get(0).getName()))
                .andExpect(jsonPath("$.data.authors[0].authorId").value(response.getAuthors().get(0).getAuthorId()))
                .andExpect(jsonPath("$.data.translators[0].name").value(response.getTranslators().get(0).getName()))
                .andExpect(jsonPath("$.data.translators[0].translatorId").value(
                        response.getTranslators().get(0).getTranslatorId()))
                .andExpect(jsonPath("$.data.starRating").value(response.getStarRating()))
                .andExpect(jsonPath("$.data.aladinStarRating").value(response.getAladinStarRating()))
                .andExpect(jsonPath("$.data.aladinReviewCount").value(response.getAladinReviewCount()))
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
                .andDo(document("book-detail-with-isbn",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("book")
                                        .summary("ISBN을 통해 도서 상세를 조회한다.")
                                        .description("쿼리 파라미터의 isbn10은 생략 가능합니다. isbn13만으로 도서 상세정보 조회가 가능합니다.")
                                        .requestHeaders(
                                                headerWithName("USER-ID").description("사용자 ID")
                                        )
                                        .queryParameters(
                                                parameterWithName("isbn10").type(SimpleType.STRING)
                                                        .description("ISBN10").optional(),
                                                parameterWithName("isbn13").type(SimpleType.STRING)
                                                        .description("ISBN13")
                                        )
                                        .responseSchema(Schema.schema("book_detail_with_isbn_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data.title").type(STRING).description("도서 제목"),
                                                fieldWithPath("data.subTitle").type(STRING).description("도서 부제목"),
                                                fieldWithPath("data.author").type(STRING).description("도서 목록"),
                                                fieldWithPath("data.thumbnail").type(STRING).description("도서 썸네일"),
                                                fieldWithPath("data.link").type(STRING).description("도서 링크"),
                                                fieldWithPath("data.authors[0].name").type(STRING)
                                                        .description("도서 저자 이름"),
                                                fieldWithPath("data.authors[0].authorId").type(NUMBER)
                                                        .description("도서 저자 ID"),
                                                fieldWithPath("data.translators[0].name").type(STRING)
                                                        .description("도서 번역자 이름"),
                                                fieldWithPath("data.translators[0].translatorId").type(NUMBER)
                                                        .description("도서 번역자 ID"),
                                                fieldWithPath("data.starRating").type(NUMBER).description("도서 별점"),
                                                fieldWithPath("data.reviewCount").type(NUMBER).description("도서 리뷰 수"),
                                                fieldWithPath("data.aladinStarRating").type(NUMBER).description("알라딘 도서 별점 수"),
                                                fieldWithPath("data.aladinReviewCount").type(NUMBER).description("알라딘 도서 리뷰 수"),
                                                fieldWithPath("data.publicationDate").type(STRING)
                                                        .description("도서 출판일"),
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

    @DisplayName("관심 도서 등록/삭제를 한다.")
    @Test
    void handleBookInterest() throws Exception {

        // given
        BookInterestHandleResponse response = BookDtoTestData.createBookInterestHandleResponse();
        String loginId = response.getUserId();
        String isbn13 = response.getIsbn13();

        given(bookInterestService.handleBookInterest(any())).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/books/{isbn13}/interest", isbn13)
                .header("USER-ID", loginId));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("관심 도서 처리에 성공했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.userId").value(response.getUserId()))
                .andExpect(jsonPath("$.data.isbn13").value(response.getIsbn13()))
                .andExpect(jsonPath("$.data.interested").value(response.isInterested()));

        // document
        actions
                .andDo(document("book-interest-handle",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("interest")
                                        .summary("관심 도서 등록/삭제를 한다.")
                                        .description("관심 도서 등록/삭제를 해당 API에서 모두 수행한다. 관심 도서인 경우 삭제를, 관심 도서가 아닌 경우 등록을 한다."
                                                + " 응답 데이터의 interested 필드를 통해 관심 도서 여부를 확인할 수 있다.")
                                        .requestHeaders(
                                                headerWithName("USER-ID").description("사용자 ID")
                                        )
                                        .pathParameters(
                                                parameterWithName("isbn13").type(SimpleType.STRING).description("도서 ISBN13")
                                        )
                                        .responseSchema(Schema.schema("book_interest_handle_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data.userId").type(STRING).description("사용자 ID"),
                                                fieldWithPath("data.isbn13").type(STRING).description("도서 ISBN13"),
                                                fieldWithPath("data.interested").type(BOOLEAN).description("관심 여부")
                                        ).build())));
    }

    @DisplayName("관심 도서를 조회한다.")
    @Test
    void getInterestBooks() throws Exception {

        // given
        BookInterestElementResponse response = BookDtoTestData.createBookInterestElementResponse();
        String loginId = "LOGIN_USER_ID";
        String userId = "USER_ID";

        given(bookInterestService.getBookInterestList(any())).willReturn(List.of(response));

        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/books/users/{userId}/interest", userId)
                .header("USER-ID", loginId)
                .param("order", "title"));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("관심 도서 목록 조회에 성공했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // document
        actions
                .andDo(document("find-all-interest-books",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("interest")
                                        .summary("관심 도서를 조회한다.")
                                        .description("쿼리 파라미터 order를 통해 정렬 순서를 지정할 수 있다. 제목순(title), 등록순(registration), 발행일순(publication)이 있다."
                                                + " 정렬이 필요없는 경우 order 파라미터를 생략할 수 있다.")
                                        .requestHeaders(
                                                headerWithName("USER-ID").description("사용자 ID")
                                        )
                                        .pathParameters(
                                                parameterWithName("userId").type(SimpleType.STRING).description("사용자 ID")
                                        )
                                        .queryParameters(
                                                parameterWithName("order").type(SimpleType.STRING).description("정렬 순서")
                                        )
                                        .responseSchema(Schema.schema("find_all_interest_books_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data[].id").type(NUMBER).description("도서 ID"),
                                                fieldWithPath("data[].title").type(STRING).description("도서 제목"),
                                                fieldWithPath("data[].author").type(STRING).description("도서 저자"),
                                                fieldWithPath("data[].isbn13").type(STRING).description("도서 ISBN13"),
                                                fieldWithPath("data[].thumbnailUrl").type(STRING).description("도서 썸네일 URL")
                                        ).build())));
    }

    @DisplayName("관심 도서 상태를 조회한다.")
    @Test
    void getInterestStatus() throws Exception {

        // given
        BookInterestStatusResponse response = BookDtoTestData.createBookInterestStatusResponse();
        String loginId = "LOGIN_USER_ID";

        given(bookInterestService.getInterestStatus(any())).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/books/{isbn13}/interest-status", "9788932917245")
                .header("USER-ID", loginId));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("관심 도서 상태 조회에 성공했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // document
        actions
                .andDo(document("get-interest-status",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("interest")
                                        .summary("관심 도서 상태를 조회한다.")
                                        .requestHeaders(
                                                headerWithName("USER-ID").description("사용자 ID")
                                        )
                                        .pathParameters(
                                                parameterWithName("isbn13").type(SimpleType.STRING).description("도서 ISBN13")
                                        )
                                        .responseSchema(Schema.schema("get_interest_status_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data.interested").type(BOOLEAN).description("관심 도서 설정 여부")
                                        ).build())));
    }
}