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
import kr.mybrary.bookservice.review.MyBookReviewDtoTestData;
import kr.mybrary.bookservice.review.domain.MyBookReviewReadService;
import kr.mybrary.bookservice.review.domain.MyBookReviewWriteService;
import kr.mybrary.bookservice.review.presentation.dto.request.MyBookReviewCreateRequest;
import kr.mybrary.bookservice.review.presentation.dto.response.ReviewsOfBookGetResponse;
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
@WebMvcTest(MyBookReviewController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class MyBookReviewControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyBookReviewWriteService myBookReviewWriteService;

    @MockBean
    private MyBookReviewReadService myBookReviewReadService;

    private static final String LOGIN_ID = "LOGIN_USER_ID";
    private static final Long MYBOOK_ID = 1L;

    @DisplayName("마이북 리뷰를 작성한다.")
    @Test
    void create() throws Exception {

        // given
        MyBookReviewCreateRequest request = MyBookReviewDtoTestData.createMyBookReviewCreateRequest();

        String requestJson = objectMapper.writeValueAsString(request);
        doNothing().when(myBookReviewWriteService).create(any());

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
        ReviewsOfBookGetResponse response = MyBookReviewDtoTestData.createReviewsOfBookGetResponse();

        given(myBookReviewReadService.getReviewsFromBook(any())).willReturn(response);

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
}