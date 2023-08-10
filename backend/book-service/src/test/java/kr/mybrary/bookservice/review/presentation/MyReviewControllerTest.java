package kr.mybrary.bookservice.review.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
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
import kr.mybrary.bookservice.review.MyReviewDtoTestData;
import kr.mybrary.bookservice.review.domain.MyReviewReadService;
import kr.mybrary.bookservice.review.domain.MyReviewWriteService;
import kr.mybrary.bookservice.review.presentation.dto.request.MyReviewCreateRequest;
import kr.mybrary.bookservice.review.presentation.dto.request.MyReviewUpdateRequest;
import kr.mybrary.bookservice.review.presentation.dto.response.MyReviewOfMyBookGetResponse;
import kr.mybrary.bookservice.review.presentation.dto.response.MyReviewUpdateResponse;
import kr.mybrary.bookservice.review.presentation.dto.response.MyReviewsOfBookGetResponse;
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
@WebMvcTest(MyReviewController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class MyReviewControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyReviewWriteService myReviewWriteService;

    @MockBean
    private MyReviewReadService myReviewReadService;

    private static final String LOGIN_ID = "LOGIN_USER_ID";
    private static final Long MYBOOK_ID = 1L;

    @DisplayName("마이북 리뷰를 작성한다.")
    @Test
    void create() throws Exception {

        // given
        MyReviewCreateRequest request = MyReviewDtoTestData.createMyBookReviewCreateRequest();

        String requestJson = objectMapper.writeValueAsString(request);
        doNothing().when(myReviewWriteService).create(any());

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/mybooks/{myBookId}/reviews", MYBOOK_ID)
                .header("USER-ID", LOGIN_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("201 CREATED"))
                .andExpect(jsonPath("$.message").value("마이 리뷰를 작성했습니다."))
                .andExpect(jsonPath("$.data").isEmpty());

        // document
        actions
                .andDo(document("create-mybook-review",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("review")
                                        .summary("마이북 리뷰를 작성한다.")
                                        .requestSchema(Schema.schema("create_mybook_review_request_body"))
                                        .pathParameters(
                                                parameterWithName("myBookId").type(SimpleType.NUMBER).description("마이북 ID")
                                        )
                                        .requestHeaders(
                                                headerWithName("USER-ID").description("사용자 ID")
                                        )
                                        .requestFields(
                                                fieldWithPath("content").type(STRING).description("마이북 리뷰 내용"),
                                                fieldWithPath("starRating").type(NUMBER).description("마이북 리뷰 별점")
                                        )
                                        .responseSchema(Schema.schema("create_mybook_review_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data").type(OBJECT).description("응답 데이터").optional()
                                        ).build())));
    }

    @DisplayName("도서의 리뷰를 모두 조회한다.")
    @Test
    void getReviewsFromBook() throws Exception {

        // given
        MyReviewsOfBookGetResponse response = MyReviewDtoTestData.createReviewsOfBookGetResponse();

        given(myReviewReadService.getReviewsFromBook(any())).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/books/{isbn13}/reviews", "9788956609959"));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("도서의 리뷰 목록입니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // document
        actions
                .andDo(document("get-review-from-book",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("review")
                                        .summary("책에 대한 리뷰를 모두 조회한다.")
                                        .requestSchema(Schema.schema("get_review_from_book_request_body"))
                                        .pathParameters(
                                                parameterWithName("isbn13").type(SimpleType.STRING).description("도서 ISBN")
                                        )
                                        .responseSchema(Schema.schema("get_review_from_book_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data.title").type(STRING).description("도서 제목"),
                                                fieldWithPath("data.isbn13").type(STRING).description("도서 ISBN13"),
                                                fieldWithPath("data.reviewCount").type(NUMBER).description("도서 리뷰 개수"),
                                                fieldWithPath("data.starRatingAverage").type(NUMBER).description("도서 리뷰 평균 별점"),
                                                fieldWithPath("data.myBookReviewList[0].id").type(NUMBER).description("리뷰 ID"),
                                                fieldWithPath("data.myBookReviewList[0].userId").type(STRING).description("리뷰 유저 ID"),
                                                fieldWithPath("data.myBookReviewList[0].userNickname").type(STRING).description("리뷰 유저 닉네임"),
                                                fieldWithPath("data.myBookReviewList[0].userPictureUrl").type(STRING).description("리뷰 유저 프로필 사진"),
                                                fieldWithPath("data.myBookReviewList[0].content").type(STRING).description("리뷰 내용"),
                                                fieldWithPath("data.myBookReviewList[0].starRating").type(NUMBER).description("리뷰 별점"),
                                                fieldWithPath("data.myBookReviewList[0].createdAt").type(STRING).description("리뷰 생성일")
                                        ).build())));
    }

    @DisplayName("마이북의 리뷰를 조회한다.")
    @Test
    void getReviewFormMyBook() throws Exception {

        // given
        MyReviewOfMyBookGetResponse response = MyReviewDtoTestData.createReviewOfMyBookGetResponse();

        given(myReviewReadService.getReviewFromMyBook(any())).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/mybooks/{myBookId}/review", 1L));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("마이북에 대한 리뷰입니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // document
        actions
                .andDo(document("get-review-from-mybook",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("review")
                                        .summary("마이북에 대한 리뷰를 조회한다. (리뷰가 없을 경우, data는 null을 반환한다.)")
                                        .requestSchema(Schema.schema("get_review_from_mybook_request_body"))
                                        .pathParameters(
                                                parameterWithName("myBookId").type(SimpleType.NUMBER).description("마이북 Id")
                                        )
                                        .responseSchema(Schema.schema("get_review_from_mybook_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data.id").type(NUMBER).description("리뷰 ID"),
                                                fieldWithPath("data.content").type(STRING).description("리뷰 내용"),
                                                fieldWithPath("data.starRating").type(NUMBER).description("리뷰 별점"),
                                                fieldWithPath("data.createdAt").type(STRING).description("리뷰 생성일"),
                                                fieldWithPath("data.updatedAt").type(STRING).description("리뷰 수정일")
                                        ).build())));
    }

    @DisplayName("마이북 리뷰를 수정한다.")
    @Test
    void update() throws Exception {

        // given
        MyReviewUpdateRequest request = MyReviewDtoTestData.createMyReviewUpdateRequest();
        MyReviewUpdateResponse response = MyReviewDtoTestData.createMyReviewUpdateResponse();

        String requestJson = objectMapper.writeValueAsString(request);

        given(myReviewWriteService.update(any())).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(put("/api/v1/reviews/{reviewId}", 1L)
                .header("USER-ID", LOGIN_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("마이 리뷰를 수정했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // document
        actions
                .andDo(document("update-mybook-review",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("review")
                                        .summary("마이북 리뷰를 수정한다.")
                                        .requestSchema(Schema.schema("update_mybook_review_request_body"))
                                        .pathParameters(
                                                parameterWithName("reviewId").type(SimpleType.NUMBER).description("마이 리뷰 ID")
                                        )
                                        .requestHeaders(
                                                headerWithName("USER-ID").description("사용자 ID")
                                        )
                                        .requestFields(
                                                fieldWithPath("content").type(STRING).description("마이 리뷰 수정 내용"),
                                                fieldWithPath("starRating").type(NUMBER).description("마이 리뷰 수정 별점")
                                        )
                                        .responseSchema(Schema.schema("update_mybook_review_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data.id").type(NUMBER).description("마이 리뷰 ID"),
                                                fieldWithPath("data.content").type(STRING).description("마이 리뷰 수정된 내용"),
                                                fieldWithPath("data.starRating").type(NUMBER).description("마이 리뷰 수정된 별점")
                                        ).build())));
    }

    @DisplayName("마이북 리뷰를 삭제한다.")
    @Test
    void delete() throws Exception {

        // given
        doNothing().when(myReviewWriteService).delete(any());

        // when
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/reviews/{reviewId}", 1L)
                .header("USER-ID", LOGIN_ID));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("마이 리뷰를 삭제했습니다."))
                .andExpect(jsonPath("$.data").isEmpty());

        // document
        actions
                .andDo(document("delete-mybook-review",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("review")
                                        .summary("마이북 리뷰를 삭제한다.")
                                        .requestSchema(Schema.schema("delete_mybook_review_request_body"))
                                        .pathParameters(
                                                parameterWithName("reviewId").type(SimpleType.NUMBER).description("마이 리뷰 ID")
                                        )
                                        .requestHeaders(
                                                headerWithName("USER-ID").description("사용자 ID")
                                        )
                                        .responseSchema(Schema.schema("delete_mybook_review_response_body"))
                                        .responseFields(
                                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                                fieldWithPath("message").type(STRING).description("응답 메시지"),
                                                fieldWithPath("data").type(OBJECT).description("응답 데이터").optional()
                                        ).build())));
    }
}