package kr.mybrary.userservice.interest.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.mybrary.userservice.interest.domain.InterestService;
import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryResponse;
import kr.mybrary.userservice.interest.domain.dto.response.InterestResponse;
import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                RestDocumentationRequestBuilders.get(BASE_URL + "/interest-categories")
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

}