package kr.mybrary.userservice.interest.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kr.mybrary.userservice.interest.InterestDtoTestData;
import kr.mybrary.userservice.interest.domain.InterestService;
import kr.mybrary.userservice.interest.domain.dto.request.UserInterestUpdateServiceRequest;
import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryResponse;
import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryServiceResponse;
import kr.mybrary.userservice.interest.domain.dto.response.InterestResponse;
import kr.mybrary.userservice.interest.domain.dto.response.UserInterestAndBookRecommendationsResponse;
import kr.mybrary.userservice.interest.domain.dto.response.UserInterestServiceResponse;
import kr.mybrary.userservice.interest.presentation.dto.request.UserInterestUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(InterestController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser(username = "loginId_1", roles = {"USER"})
@AutoConfigureRestDocs
class InterestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InterestService interestService;

    @Autowired
    private MockMvc mockMvc;

    private final String BASE_URL = "/api/v1";
    private final String USER_ID = "userId";
    private final String STATUS_FIELD_DESCRIPTION = "응답 상태";
    private final String MESSAGE_FIELD_DESCRIPTION = "응답 메시지";

    @DisplayName("카테고리별 관심사를 모두 조회한다.")
    @Test
    void getInterestsGroupByCategory() throws Exception {
        // given
        InterestCategoryServiceResponse interestCategoryServiceResponse = InterestCategoryServiceResponse.builder()
                .interestCategories(
                        List.of(
                                InterestCategoryResponse.builder()
                                        .id(1L)
                                        .name("문학 감성")
                                        .description("문학 감성을 좋아하시나요?")
                                        .interestResponses(List.of(
                                                InterestResponse.builder()
                                                        .id(1L)
                                                        .name("국내소설")
                                                        .build(),
                                                InterestResponse.builder()
                                                        .id(2L)
                                                        .name("외국소설")
                                                        .build()
                                        ))
                                        .build(),
                                InterestCategoryResponse.builder()
                                        .id(2L)
                                        .name("학습 및 성장")
                                        .description("학습 및 성장을 갈망하고 계신가요?")
                                        .interestResponses(List.of(
                                                InterestResponse.builder()
                                                        .id(3L)
                                                        .name("자기계발")
                                                        .build(),
                                                InterestResponse.builder()
                                                        .id(4L)
                                                        .name("과학")
                                                        .build()
                                        ))
                                        .build()
                        )
                )
                .build();

        given(interestService.getInterestCategories()).willReturn(interestCategoryServiceResponse);

        // when
        ResultActions actions = mockMvc.perform(
                get(BASE_URL + "/interest-categories")
                        .with(csrf()));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("카테고리별 관심사를 모두 조회했습니다."))
                .andExpect(jsonPath("$.data.interestCategories[0].id").value(1L))
                .andExpect(jsonPath("$.data.interestCategories[0].name").value("문학 감성"))
                .andExpect(jsonPath("$.data.interestCategories[0].description").value("문학 감성을 좋아하시나요?"))
                .andExpect(jsonPath("$.data.interestCategories[0].interestResponses[0].id").value(1L))
                .andExpect(jsonPath("$.data.interestCategories[0].interestResponses[0].name").value("국내소설"))
                .andExpect(jsonPath("$.data.interestCategories[0].interestResponses[1].id").value(2L))
                .andExpect(jsonPath("$.data.interestCategories[0].interestResponses[1].name").value("외국소설"))
                .andExpect(jsonPath("$.data.interestCategories[1].id").value(2L))
                .andExpect(jsonPath("$.data.interestCategories[1].name").value("학습 및 성장"))
                .andExpect(jsonPath("$.data.interestCategories[1].description").value("학습 및 성장을 갈망하고 계신가요?"))
                .andExpect(jsonPath("$.data.interestCategories[1].interestResponses[0].id").value(3L))
                .andExpect(jsonPath("$.data.interestCategories[1].interestResponses[0].name").value("자기계발"))
                .andExpect(jsonPath("$.data.interestCategories[1].interestResponses[1].id").value(4L))
                .andExpect(jsonPath("$.data.interestCategories[1].interestResponses[1].name").value("과학"));

        verify(interestService).getInterestCategories();

        // docs
        actions.andDo(document("get-interest-categories",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-interest")
                                .summary("카테고리별 관심사를 모두 조회한다.")
                                .responseSchema(Schema.schema("get_interest_categories_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING).description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data.interestCategories[].id").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                                        fieldWithPath("data.interestCategories[].name").type(JsonFieldType.STRING).description("카테고리 이름"),
                                        fieldWithPath("data.interestCategories[].description").type(JsonFieldType.STRING).description("카테고리 설명"),
                                        fieldWithPath("data.interestCategories[].interestResponses[].id").type(JsonFieldType.NUMBER).description("관심사 ID"),
                                        fieldWithPath("data.interestCategories[].interestResponses[].name").type(JsonFieldType.STRING).description("관심사 이름")
                                )
                                .build()
                ))
        );
    }

    @DisplayName("사용자의 관심사를 모두 조회한다.")
    @Test
    void getUserInterests() throws Exception {
        // given
        UserInterestServiceResponse interestServiceResponse = UserInterestServiceResponse.builder()
                .userId(USER_ID)
                .userInterests(List.of(
                        InterestResponse.builder()
                                .id(1L)
                                .name("국내소설")
                                .build(),
                        InterestResponse.builder()
                                .id(2L)
                                .name("외국소설")
                                .build()
                ))
                .build();

        given(interestService.getUserInterests(USER_ID)).willReturn(interestServiceResponse);

        // when
        ResultActions actions = mockMvc.perform(
                get(BASE_URL + "/users/{userId}/interests", USER_ID)
                        .with(csrf()));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("사용자의 관심사를 모두 조회했습니다."))
                .andExpect(jsonPath("$.data.userId").value(USER_ID))
                .andExpect(jsonPath("$.data.userInterests[0].id").value(1L))
                .andExpect(jsonPath("$.data.userInterests[0].name").value("국내소설"))
                .andExpect(jsonPath("$.data.userInterests[1].id").value(2L))
                .andExpect(jsonPath("$.data.userInterests[1].name").value("외국소설"));

        verify(interestService).getUserInterests(USER_ID);

        // docs
        actions.andDo(document("get-user-interests",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-interest")
                                .summary("사용자의 관심사를 모두 조회한다.")
                                .pathParameters(
                                        parameterWithName("userId").description("사용자 ID")
                                )
                                .responseSchema(Schema.schema("get_user_interests_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING).description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data.userId").type(JsonFieldType.STRING).description("사용자 ID"),
                                        fieldWithPath("data.userInterests[].id").type(JsonFieldType.NUMBER).description("관심사 ID"),
                                        fieldWithPath("data.userInterests[].name").type(JsonFieldType.STRING).description("관심사 이름")
                                )
                                .build()
                ))
        );
    }

    @DisplayName("사용자의 관심사를 수정한다.")
    @Test
    void putUserInterests() throws Exception {
        // given
        UserInterestUpdateRequest updateRequest = UserInterestUpdateRequest.builder()
                .interestRequests(
                        List.of(
                                UserInterestUpdateRequest.InterestRequest.builder()
                                        .id(10L)
                                        .name("IT")
                                        .build(),
                                UserInterestUpdateRequest.InterestRequest.builder()
                                        .id(11L)
                                        .name("경제경영")
                                        .build(),
                                UserInterestUpdateRequest.InterestRequest.builder()
                                        .id(12L)
                                        .name("사회과학")
                                        .build()
                        )
                )
                .build();

        UserInterestServiceResponse interestServiceResponse = UserInterestServiceResponse.builder()
                .userId(USER_ID)
                .userInterests(List.of(
                        InterestResponse.builder()
                                .id(10L)
                                .name("IT")
                                .build(),
                        InterestResponse.builder()
                                .id(11L)
                                .name("경제경영")
                                .build(),
                        InterestResponse.builder()
                                .id(12L)
                                .name("사회과학")
                                .build()
                ))
                .build();

        given(interestService.updateUserInterests(any(UserInterestUpdateServiceRequest.class))).willReturn(interestServiceResponse);

        // when
        ResultActions actions = mockMvc.perform(
                put(BASE_URL + "/users/{userId}/interests", USER_ID)
                        .with(csrf())
                        .header("USER-ID", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("사용자의 관심사를 수정했습니다."))
                .andExpect(jsonPath("$.data.userId").value(USER_ID))
                .andExpect(jsonPath("$.data.userInterests[0].id").value(10L))
                .andExpect(jsonPath("$.data.userInterests[0].name").value("IT"))
                .andExpect(jsonPath("$.data.userInterests[1].id").value(11L))
                .andExpect(jsonPath("$.data.userInterests[1].name").value("경제경영"))
                .andExpect(jsonPath("$.data.userInterests[2].id").value(12L))
                .andExpect(jsonPath("$.data.userInterests[2].name").value("사회과학"));

        // docs
        actions.andDo(document("put-user-interests",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-interest")
                                .summary("사용자의 관심사를 수정한다.")
                                .description("관심사는 최대 3개까지 설정할 수 있으며, 중복된 관심사는 설정할 수 없다.")
                                .pathParameters(
                                        parameterWithName("userId").description("사용자 ID")
                                )
                                .requestSchema(Schema.schema("put_user_interests_request_body"))
                                .requestHeaders(
                                        headerWithName("USER-ID").description("로그인 된 사용자의 아이디")
                                )
                                .requestFields(
                                        fieldWithPath("interestRequests[].id").type(JsonFieldType.NUMBER).description("관심사 ID"),
                                        fieldWithPath("interestRequests[].name").type(JsonFieldType.STRING).description("관심사 이름")
                                )
                                .responseSchema(Schema.schema("put_user_interests_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING).description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data.userId").type(JsonFieldType.STRING).description("사용자 ID"),
                                        fieldWithPath("data.userInterests[].id").type(JsonFieldType.NUMBER).description("관심사 ID"),
                                        fieldWithPath("data.userInterests[].name").type(JsonFieldType.STRING).description("관심사 이름")
                                )
                                .build()
                ))
        );
    }

    @DisplayName("관심사와 책 추천을 함께 조회한다.")
    @Test
    void getInterestsAndBookRecommendations() throws Exception {

        // given
        String type = "bestseller";
        int page = 1;

        UserInterestAndBookRecommendationsResponse response = InterestDtoTestData.createUserInterestAndBookRecommendationsResponse();
        given(interestService.getInterestsAndBookRecommendations(any())).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(get(BASE_URL + "/interests/book-recommendations/{type}", type)
                        .param("page", String.valueOf(page))
                        .header("USER-ID", USER_ID));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("사용자의 모든 관심사와 그 중 하나의 관심사에 대한 추천 도서를 조회했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // docs
        actions.andDo(document("get-interests-and-book-recommendations",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-interest-and-book-recommendations")
                                .summary("관심사와 책 추천을 함께 조회한다.")
                                .description("사용자의 모든 관심사와 그 중 첫번쨰 관심사에 대한 추천 도서를 조회한다. 관심사가 없는 경우 빈 리스트를 반환한다. page 생략시 1 페이지를 반환합니다.")
                                .pathParameters(
                                        parameterWithName("type").description("책 추천 타입")
                                )
                                .requestHeaders(
                                        headerWithName("USER-ID").description("로그인 된 사용자의 아이디")
                                )
                                .queryParameters(
                                        parameterWithName("page").description("페이지 번호").optional()
                                )
                                .responseSchema(Schema.schema("get_interests_and_book_recommendations_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING).description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data.userInterests[].name").type(JsonFieldType.STRING).description("관심사 이름"),
                                        fieldWithPath("data.userInterests[].code").type(JsonFieldType.NUMBER).description("관심사 코드"),
                                        fieldWithPath("data.bookRecommendations[].thumbnailUrl").type(JsonFieldType.STRING).description("추천 도서 썸네일 URL"),
                                        fieldWithPath("data.bookRecommendations[].isbn13").type(JsonFieldType.STRING).description("추천 도서 ISBN13")
                                )
                                .build()
                ))
        );
    }
}