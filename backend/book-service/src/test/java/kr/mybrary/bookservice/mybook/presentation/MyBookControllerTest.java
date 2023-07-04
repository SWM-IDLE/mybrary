package kr.mybrary.bookservice.mybook.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.mybook.domain.MyBookService;
import kr.mybrary.bookservice.mybook.presentation.dto.request.MyBookCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MyBookController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class MyBookControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyBookService myBookService;

    @DisplayName("내 서재에 책을 추가한다.")
    @Test
    void assignMyBook() throws Exception {
        // given
        MyBookCreateRequest request = createMyBookCreateRequest();

        String requestJson = objectMapper.writeValueAsString(request);

        given(myBookService.create(request.toServiceRequest("userId"))).willReturn(any());

        // when
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/mybooks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("201 CREATED"))
                .andExpect(jsonPath("$.message").value("내 서재에 도서를 등록했습니다."))
                .andExpect(jsonPath("$.data").isEmpty());

        // document
        actions
                .andDo(document("create-mybook",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("mybook")
                                        .summary("마이북으로 등록한다.")
                                        .requestSchema(Schema.schema("create-mybook request body"))
                                        .requestFields(
                                                fieldWithPath("title").type(STRING).description("도서 제목"),
                                                fieldWithPath("description").type(STRING).description("도서 설명"),
                                                fieldWithPath("detailsUrl").type(STRING).description("도서 상세 URL"),
                                                fieldWithPath("isbn10").type(STRING).description("도서 ISBN10"),
                                                fieldWithPath("isbn13").type(STRING).description("도서 ISBN13"),
                                                fieldWithPath("price").type(NUMBER).description("도서 가격"),
                                                fieldWithPath("authors").type(ARRAY).description("도서 저자"),
                                                fieldWithPath("translators").type(ARRAY).description("도서 번역자"),
                                                fieldWithPath("publisher").type(STRING).description("출판사"),
                                                fieldWithPath("publicationDate").type(STRING).description("출판일"),
                                                fieldWithPath("thumbnailUrl").type(STRING).description("썸네일 URL")
                                        )
                                        .responseSchema(Schema.schema("create-mybook response body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data").type(OBJECT).description("응답 데이터").optional()
                                        ).build())));
    }

    private MyBookCreateRequest createMyBookCreateRequest() {
        return MyBookCreateRequest.builder()
                .title("title")
                .description("description")
                .detailsUrl("detailsUrl")
                .isbn10("isbn10")
                .isbn13("isbn13")
                .publisher("publisher")
                .price(10000)
                .publicationDate(LocalDateTime.now())
                .translators(List.of("translator1", "translator2"))
                .authors(List.of("author1", "author2"))
                .thumbnailUrl("thumbnailUrl")
                .build();
    }
}