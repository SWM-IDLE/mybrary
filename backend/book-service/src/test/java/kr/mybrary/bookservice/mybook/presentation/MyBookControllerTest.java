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
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookElementFromMeaningTagResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookElementResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookReadCompletedStatusResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookRegisteredStatusResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookRegistrationCountResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookUpdateResponse;
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
        given(myBookService.create(any())).willReturn(any());

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
                                        .requestSchema(Schema.schema("create_mybook_request_body"))
                                        .requestHeaders(
                                                headerWithName("USER-ID").description("사용자 ID")
                                        )
                                        .requestFields(
                                                fieldWithPath("isbn13").type(STRING).description("도서 ISBN13")
                                        )
                                        .responseSchema(Schema.schema("create_mybook_response_body"))
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
                .param("order", "title")
                .param("readStatus", "TO_READ")
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
                                .description("쿼리 파라미터 order를 통해 정렬 순서를 지정할 수 있다. 제목순(title), 등록순(registration), 발행일순(publication)이 있다."
                                        + " 정렬이 필요없는 경우 order 파라미터를 생략할 수 있다."
                                        + " 또한 쿼리 파라미터 readStatus를 통해 읽은 상태 필터링을 할 수 있다. 읽기전(TO_READ), 읽는중(READING), 완독(COMPLETED)이 있다."
                                        + " 읽은 상태 필터링이 필요없는 경우 readStatus 파라미터를 생략할 수 있다.")
                                .pathParameters(
                                        parameterWithName("userId").type(SimpleType.STRING).description("사용자 ID")
                                )
                                .requestHeaders(
                                        headerWithName("USER-ID").description("사용자 ID")
                                )
                                .queryParameters(
                                        parameterWithName("order").type(SimpleType.STRING).description("정렬 순서"),
                                        parameterWithName("readStatus").type(SimpleType.STRING).description("읽은 상태 필터 조건")
                                )
                                .responseSchema(Schema.schema("find_all_mybooks_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(STRING).description("응답 상태"),
                                        fieldWithPath("message").type(STRING).description("응답 메시지"),
                                        fieldWithPath("data[].id").type(NUMBER).description("마이북 ID"),
                                        fieldWithPath("data[].readStatus").type(STRING).description("독서 진행 상태"),
                                        fieldWithPath("data[].startDateOfPossession").type(STRING).description("보유 시작일"),
                                        fieldWithPath("data[].showable").type(BOOLEAN).description("공개 여부"),
                                        fieldWithPath("data[].exchangeable").type(BOOLEAN).description("교환 여부"),
                                        fieldWithPath("data[].shareable").type(BOOLEAN).description("나눔 여부"),
                                        fieldWithPath("data[].book.id").type(NUMBER).description("도서 ID"),
                                        fieldWithPath("data[].book.title").type(STRING).description("도서 제목"),
                                        fieldWithPath("data[].book.description").type(STRING).description("도서 설명"),
                                        fieldWithPath("data[].book.thumbnailUrl").type(STRING).description("도서 썸네일 URL"),
                                        fieldWithPath("data[].book.starRating").type(NUMBER).description("도서 별점"),
                                        fieldWithPath("data[].book.authors").type(STRING).description("도서 저자 이름 (, 로 분리)"),
                                        fieldWithPath("data[].book.publicationDate").type(STRING).description("도서 출판일")
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
                                        .responseSchema(Schema.schema("find_mybook_detail_response_body"))
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
                                                fieldWithPath("data.book.isbn13").type(STRING).description("도서 ISBN13"),
                                                fieldWithPath("data.book.title").type(STRING).description("도서 제목"),
                                                fieldWithPath("data.book.description").type(STRING).description("도서 설명"),
                                                fieldWithPath("data.book.thumbnailUrl").type(STRING).description("도서 썸네일 URL"),
                                                fieldWithPath("data.book.authors").type(ARRAY).description("도서 저자"),
                                                fieldWithPath("data.book.translators").type(ARRAY).description("도서 번역자"),
                                                fieldWithPath("data.book.starRating").type(NUMBER).description("도서 별점"),
                                                fieldWithPath("data.book.publisher").type(STRING).description("출판사"),
                                                fieldWithPath("data.meaningTag.quote").type(STRING).description("의미 태그 문구"),
                                                fieldWithPath("data.meaningTag.colorCode").type(STRING).description("의미 태그 색상")
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
                                        .responseSchema(Schema.schema("delete_mybook_response_body"))
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
                                        .description("의미 태그 색상은 hex code로 입력한다.")
                                        .requestSchema(Schema.schema("update_mybook_properties_request_body"))
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
                                                fieldWithPath("startDateOfPossession").type(STRING).description("보유 시작일"),
                                                fieldWithPath("meaningTag.quote").type(STRING).description("의미 태그 문구"),
                                                fieldWithPath("meaningTag.colorCode").type(STRING).description("의미 태그 색상")
                                        )
                                        .responseSchema(Schema.schema("update_mybook_properties_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data.showable").type(BOOLEAN).description("공개 여부"),
                                                fieldWithPath("data.exchangeable").type(BOOLEAN).description("교환 여부"),
                                                fieldWithPath("data.shareable").type(BOOLEAN).description("나눔 여부"),
                                                fieldWithPath("data.readStatus").type(STRING).description("독서 진행 상태"),
                                                fieldWithPath("data.startDateOfPossession").type(STRING).description("보유 시작일"),
                                                fieldWithPath("data.meaningTag.quote").type(STRING).description("의미 태그 문구"),
                                                fieldWithPath("data.meaningTag.colorCode").type(STRING).description("의미 태그 색상")
                                        ).build())));
    }

    @DisplayName("의미 태그를 통해서 마이북을 조회한다.")
    @Test
    void getMyBookByMeaningTag() throws Exception {

        // given
        MyBookElementFromMeaningTagResponse expectedResponse_1 = MybookDtoTestData.createMyBookElementFromMeaningTagResponse();
        MyBookElementFromMeaningTagResponse expectedResponse_2 = MybookDtoTestData.createMyBookElementFromMeaningTagResponse();

        given(myBookService.findByMeaningTagQuote(any())).willReturn(List.of(expectedResponse_1, expectedResponse_2));

        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/mybooks/meaning-tags/{meaningTagQuote}", "의미 태그 문구 예시")
                        .header("USER-ID", LOGIN_ID));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("의미 태그를 통해서 마이북을 조회했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // document
        actions.andDo(document("get-mybook-by-meaning-tag-quote",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("mybook")
                                        .summary("의미 태그를 통해서 마이북을 조회한다.")
                                        .requestHeaders(
                                                headerWithName("USER-ID").description("사용자 ID")
                                        )
                                        .pathParameters(
                                                parameterWithName("meaningTagQuote").type(SimpleType.STRING).description("의미 태그 문구")
                                        )
                                        .responseSchema(Schema.schema("get_mybook_by_meaning_tag_quote_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data[].id").type(NUMBER).description("마이북 ID"),
                                                fieldWithPath("data[].readStatus").type(STRING).description("독서 진행 상태"),
                                                fieldWithPath("data[].startDateOfPossession").type(STRING).description("보유 시작일"),
                                                fieldWithPath("data[].showable").type(BOOLEAN).description("공개 여부"),
                                                fieldWithPath("data[].exchangeable").type(BOOLEAN).description("교환 여부"),
                                                fieldWithPath("data[].shareable").type(BOOLEAN).description("나눔 여부"),
                                                fieldWithPath("data[].book.id").type(NUMBER).description("도서 ID"),
                                                fieldWithPath("data[].book.title").type(STRING).description("도서 제목"),
                                                fieldWithPath("data[].book.description").type(STRING).description("도서 설명"),
                                                fieldWithPath("data[].book.thumbnailUrl").type(STRING).description("도서 썸네일 URL"),
                                                fieldWithPath("data[].book.starRating").type(NUMBER).description("도서 별점"),
                                                fieldWithPath("data[].book.publicationDate").type(STRING).description("도서 출판일"))
                                        .build())));
    }

    @DisplayName("오늘 마이북 등록수를 조회한다.")
    @Test
    void getTodayRegistrationCount() throws Exception {

        // given
        MyBookRegistrationCountResponse response = MybookDtoTestData.createMyBookRegistrationCountResponse();
        given(myBookService.getBookRegistrationCountOfToday()).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/mybooks/today-registration-count"));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("오늘의 마이북 등록 수입니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // document
        actions.andDo(document("get-today-mybook-registration-count",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("mybook")
                                .summary("오늘 마이북 등록수를 조회한다.")
                                .responseSchema(Schema.schema("get_today_mybook_registration_count_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(STRING).description("응답 상태"),
                                        fieldWithPath("message").type(STRING).description("응답 메시지"),
                                        fieldWithPath("data.count").type(NUMBER).description("오늘의 마이북 등록 수"))
                                .build())));
    }

    @DisplayName("마이북 등록 상태를 조회한다.")
    @Test
    void getMyBookRegisteredStatus() throws Exception {

        // given
        MyBookRegisteredStatusResponse response = MybookDtoTestData.createMyBookRegisteredStatusResponse();
        String loginId = "LOGIN_USER_ID";

        given(myBookService.getMyBookRegisteredStatus(any())).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/books/{isbn13}/mybook-registered-status", "9788932917245")
                .header("USER-ID", loginId));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("해당 도서의 마이북 등록 상태 여부 입니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // document
        actions
                .andDo(document("get-mybook-registered-status",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("mybook")
                                        .summary("마이북 등록 여부 를 조회한다.")
                                        .requestHeaders(
                                                headerWithName("USER-ID").description("사용자 ID")
                                        )
                                        .pathParameters(
                                                parameterWithName("isbn13").type(SimpleType.STRING).description("도서 ISBN13")
                                        )
                                        .responseSchema(Schema.schema("get_mybook_registered_status_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data.registered").type(SimpleType.BOOLEAN).description("마이북 등록 여부")
                                        ).build())));
    }

    @DisplayName("도서 완독 상태를 조회한다.")
    @Test
    void getReadCompletedStatus() throws Exception {

        // given
        MyBookReadCompletedStatusResponse response = MybookDtoTestData.createMyBookReadCompletedStatusResponse();
        String loginId = "LOGIN_USER_ID";

        given(myBookService.getMyBookReadCompletedStatus(any())).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/books/{isbn13}/read-complete-status", "9788932917245")
                .header("USER-ID", loginId));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("해당 도서 완독 여부 입니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // document
        actions
                .andDo(document("get-read-completed-status",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("mybook")
                                        .summary("도서 완독 여부를 조회한다.")
                                        .requestHeaders(
                                                headerWithName("USER-ID").description("사용자 ID")
                                        )
                                        .pathParameters(
                                                parameterWithName("isbn13").type(SimpleType.STRING).description("도서 ISBN13")
                                        )
                                        .responseSchema(Schema.schema("get_read_completed_status_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data.completed").type(SimpleType.BOOLEAN).description("도서 완독 여부")
                                        ).build())));
    }
}