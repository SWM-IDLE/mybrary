package kr.mybrary.bookservice.mybook.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
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
import kr.mybrary.bookservice.mybook.MybookDtoTestData;
import kr.mybrary.bookservice.mybook.domain.MyBookService;
import kr.mybrary.bookservice.mybook.presentation.dto.request.MyBookCreateRequest;
import kr.mybrary.bookservice.mybook.presentation.dto.request.MyBookUpdateRequest;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookDetailResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookElementResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookUpdateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
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

    private static final String LOGIN_ID = "LOGIN_USER_ID";
    private static final Long MYBOOK_ID = 1L;

    @DisplayName("내 서재에 책을 추가한다.")
    @Test
    void createMyBook() throws Exception {
        // given
        MyBookCreateRequest request = MybookDtoTestData.createMyBookCreateRequest();

        String requestJson = objectMapper.writeValueAsString(request);

        given(myBookService.create(request.toServiceRequest(any()))).willReturn(any());

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/mybooks")
                .header("USER-ID", LOGIN_ID)
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
                                        .requestHeaders(
                                                headerWithName("USER-ID").description("사용자 ID")
                                        )
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

    @DisplayName("서재의 도서를 모두 조회한다.")
    @Test
    void findAllMybooks() throws Exception {
        // given
        MyBookElementResponse expectedResponse_1 = MybookDtoTestData.createMyBookElementResponse();
        MyBookElementResponse expectedResponse_2 = MybookDtoTestData.createMyBookElementResponse();

        given(myBookService.findAllMyBooks(any())).willReturn(List.of(expectedResponse_1, expectedResponse_2));

        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/users/{userId}/mybooks", LOGIN_ID)
                .header("USER-ID", LOGIN_ID));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("서재의 도서 목록입니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // document
        actions.andDo(document("find-all-mybooks",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("mybook")
                                .summary("내 서재의 도서를 모두 조회한다.")
                                .pathParameters(
                                        parameterWithName("userId").type(SimpleType.STRING).description("사용자 ID")
                                )
                                .requestHeaders(
                                        headerWithName("USER-ID").description("사용자 ID")
                                )
                                .responseSchema(Schema.schema("find-all-mybooks response body"))
                                .responseFields(
                                        fieldWithPath("status").type(STRING).description("응답 상태"),
                                        fieldWithPath("message").type(STRING).description("응답 메시지"),
                                        fieldWithPath("data[].id").type(NUMBER).description("마이북 ID"),
                                        fieldWithPath("data[].readStatus").type(STRING).description("독서 진행 상태"),
                                        fieldWithPath("data[].startDateOfPossession").type(STRING).description("보유 시작일"),
                                        fieldWithPath("data[].book.id").type(NUMBER).description("도서 ID"),
                                        fieldWithPath("data[].book.title").type(STRING).description("도서 제목"),
                                        fieldWithPath("data[].book.description").type(STRING).description("도서 설명"),
                                        fieldWithPath("data[].book.thumbnailUrl").type(STRING).description("도서 썸네일 URL"),
                                        fieldWithPath("data[].book.stars").type(NUMBER).description("도서 별점"),
                                        fieldWithPath("data[].showable").type(BOOLEAN).description("공개 여부"),
                                        fieldWithPath("data[].exchangeable").type(BOOLEAN).description("교환 여부"),
                                        fieldWithPath("data[].shareable").type(BOOLEAN).description("나눔 여부")
                                ).build())));

    }

    @DisplayName("내 마이북의 상세보기를 조회한다.")
    @Test
    void findMyBookDetail() throws Exception {

        // given
        MyBookDetailResponse expectedResponse = MybookDtoTestData.createMyBookDetailResponse();

        given(myBookService.findMyBookDetail(any())).willReturn(expectedResponse);

        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/mybooks/{mybookId}", MYBOOK_ID)
                .header("USER-ID", LOGIN_ID));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("마이북 상세보기입니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // document
        actions
                .andDo(document("find-mybook-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("mybook")
                                        .summary("내 마이북의 상세보기를 조회한다.")
                                        .requestHeaders(
                                                headerWithName("USER-ID").description("사용자 ID")
                                        )
                                        .pathParameters(
                                                parameterWithName("mybookId").type(SimpleType.NUMBER).description("마이북 ID")
                                        )
                                        .responseSchema(Schema.schema("find-mybook-detail response body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data.id").type(NUMBER).description("마이북 ID"),
                                                fieldWithPath("data.showable").type(BOOLEAN).description("공개 여부"),
                                                fieldWithPath("data.exchangeable").type(BOOLEAN).description("교환 여부"),
                                                fieldWithPath("data.shareable").type(BOOLEAN).description("나눔 여부"),
                                                fieldWithPath("data.readStatus").type(STRING).description("독서 진행 상태"),
                                                fieldWithPath("data.startDateOfPossession").type(STRING).description("보유 시작일"),
                                                fieldWithPath("data.book.id").type(NUMBER).description("도서 ID"),
                                                fieldWithPath("data.book.title").type(STRING).description("도서 제목"),
                                                fieldWithPath("data.book.description").type(STRING).description("도서 설명"),
                                                fieldWithPath("data.book.thumbnailUrl").type(STRING).description("도서 썸네일 URL"),
                                                fieldWithPath("data.book.authors").type(ARRAY).description("도서 저자"),
                                                fieldWithPath("data.book.translators").type(ARRAY).description("도서 번역자"),
                                                fieldWithPath("data.book.stars").type(NUMBER).description("도서 별점"),
                                                fieldWithPath("data.book.publisher").type(STRING).description("출판사")
                                        ).build())));
    }

    @DisplayName("마이북을 삭제한다.")
    @Test
    void deleteMyBook() throws Exception {

        // given
        Long id = 1L;

        // when
        ResultActions actions = mockMvc.perform(delete("/api/v1/mybooks/{id}", id)
                .header("USER-ID", LOGIN_ID));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("내 서재의 도서를 삭제했습니다."))
                .andExpect(jsonPath("$.data").isEmpty());

        // document
        actions
                .andDo(document("delete-mybook",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("mybook")
                                        .summary("내 서재의 마이북을 삭제한다.")
                                        .requestHeaders(
                                                headerWithName("USER-ID").description("사용자 ID")
                                        )
                                        .pathParameters(
                                                parameterWithName("id").type(SimpleType.INTEGER).description("마이북 ID")
                                        )
                                        .responseSchema(Schema.schema("delete-mybook response body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data").type(OBJECT).description("응답 데이터").optional()
                                        ).build())));
    }

    @DisplayName("내 서재의 마이북 속성을 수정한다.")
    @Test
    void updateMyBookProperties() throws Exception {

        // given
        MyBookUpdateRequest request = MybookDtoTestData.createMyBookUpdateRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        MyBookUpdateResponse expectedResponse = MybookDtoTestData.createMyBookUpdateResponse();

        given(myBookService.updateMyBookProperties(any())).willReturn(expectedResponse);

        // when
        ResultActions actions = mockMvc.perform(put("/api/v1/mybooks/{mybookId}", MYBOOK_ID)
                .header("USER-ID", LOGIN_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("내 서재의 마이북 속성을 수정했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // document
        actions
                .andDo(document("update-mybook-properties",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("mybook")
                                        .summary("마이북 속성을 수정한다.")
                                        .requestSchema(Schema.schema("update-mybook-properties request body"))
                                        .requestHeaders(
                                                headerWithName("USER-ID").description("사용자 ID")
                                        )
                                        .pathParameters(
                                                parameterWithName("mybookId").type(SimpleType.INTEGER).description("마이북 ID")
                                        )
                                        .requestFields(
                                                fieldWithPath("showable").type(BOOLEAN).description("공개 여부"),
                                                fieldWithPath("exchangeable").type(BOOLEAN).description("교환 여부"),
                                                fieldWithPath("shareable").type(BOOLEAN).description("나눔 여부"),
                                                fieldWithPath("readStatus").type(STRING).description("독서 진행 상태"),
                                                fieldWithPath("startDateOfPossession").type(STRING).description("보유 시작일")
                                        )
                                        .responseSchema(Schema.schema("update-mybook-properties response body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data.showable").type(BOOLEAN).description("공개 여부"),
                                                fieldWithPath("data.exchangeable").type(BOOLEAN).description("교환 여부"),
                                                fieldWithPath("data.shareable").type(BOOLEAN).description("나눔 여부"),
                                                fieldWithPath("data.readStatus").type(STRING).description("독서 진행 상태"),
                                                fieldWithPath("data.startDateOfPossession").type(STRING).description("보유 시작일")
                                        ).build())));
    }
}