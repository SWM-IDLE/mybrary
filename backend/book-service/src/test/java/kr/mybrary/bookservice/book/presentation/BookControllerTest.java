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
                                        fieldWithPath("description").type(STRING).description("도서 설명"),
                                        fieldWithPath("detailsUrl").type(STRING).description("도서 상세 URL"),
                                        fieldWithPath("isbn10").type(STRING).description("도서 ISBN10"),
                                        fieldWithPath("isbn13").type(STRING).description("도서 ISBN13"),
                                        fieldWithPath("price").type(NUMBER).description("도서 가격"),
                                        fieldWithPath("translators").type(ARRAY).description("도서 번역가"),
                                        fieldWithPath("authors").type(ARRAY).description("도서 저자"),
                                        fieldWithPath("publisher").type(STRING).description("출판사"),
                                        fieldWithPath("publicationDate").type(STRING).description("출판일"),
                                        fieldWithPath("thumbnailUrl").type(STRING).description("도서 썸네일 URL")
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