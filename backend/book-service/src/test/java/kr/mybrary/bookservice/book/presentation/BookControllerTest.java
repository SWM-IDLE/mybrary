package kr.mybrary.bookservice.book.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.mybrary.bookservice.book.BookDtoTestData;
import kr.mybrary.bookservice.book.domain.BookService;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.presentation.dto.request.BookCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
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
    private BookService bookService;

    @DisplayName("새로운 도서를 등록한다.")
    @Test
    void create() throws Exception {

        // given
        BookCreateRequest request = BookDtoTestData.createBookCreateRequest();

        String requestJson = objectMapper.writeValueAsString(request);

        given(bookService.getRegisteredBook(request.toServiceRequest()))
                .willReturn(any(Book.class));

        // when
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/books")
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
                                        fieldWithPath("thumbnailUrl").type(STRING).description("도서 썸네일 URL"),
                                        fieldWithPath("link").type(STRING).description("도서 링크"),
                                        fieldWithPath("translators").type(ARRAY).description("도서 번역가"),
                                        fieldWithPath("translators[].name").type(STRING).description("도서 번역가 이름"),
                                        fieldWithPath("translators[].translatorId").type(NUMBER).description("도서 번역가 ID"),
                                        fieldWithPath("authors").type(ARRAY).description("도서 저자"),
                                        fieldWithPath("authors[].name").type(STRING).description("도서 저자 이름"),
                                        fieldWithPath("authors[].authorId").type(NUMBER).description("도서 저자 ID"),
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
}