package kr.mybrary.bookservice.tag.presentation;


import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import java.util.List;
import kr.mybrary.bookservice.tag.MeaningTagDtoTestData;
import kr.mybrary.bookservice.tag.domain.MeaningTagService;
import kr.mybrary.bookservice.tag.presentation.dto.response.MeaningTagElementResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
@WebMvcTest(MeaningTagController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class MeaningTagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeaningTagService meaningTagService;

    private static final String LOGIN_ID = "LOGIN_USER_ID";

    @DisplayName("의미 태그 페이징 조회한다.")
    @Test
    void getPageMeaningTags() throws Exception {
        // given
        int size = 10;
        MeaningTagElementResponse response_1 = MeaningTagDtoTestData.createMeaningTagElementResponse(1L, 20);
        MeaningTagElementResponse response_2 = MeaningTagDtoTestData.createMeaningTagElementResponse(2L, 10);

        given(meaningTagService.findPageOrderByRegisteredCount(any()))
                .willReturn(List.of(response_1, response_2));

        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/meaning-tags/most")
                .param("size", String.valueOf(size))
                .header("USER-ID", LOGIN_ID));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("등록된 수가 큰 순서대로 의미 태그 페이징 조회 결과입니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // document
        actions.andDo(document("find-paging-meaning-tags-sort-by-registered-count",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("meaningTag")
                                .summary("등록된 수가 큰 순서대로 의미 태그 페이징 조회한다.")
                                .description("등록된 수가 많은 순으로 의미 태그가 페이징하여 조회된다. size 값에 따라 조회되는 수가 달라지며, size는 1보다 항상 커야 한다.")
                                .queryParameters(
                                        parameterWithName("size").type(SimpleType.INTEGER).description("페이지 크기")
                                )
                                .requestHeaders(
                                        headerWithName("USER-ID").description("사용자 ID")
                                )
                                .responseSchema(Schema.schema("find_paging_meaning_tags_sort_by_registered_count_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(STRING).description("응답 상태"),
                                        fieldWithPath("message").type(STRING).description("응답 메시지"),
                                        fieldWithPath("data[].id").type(NUMBER).description("의미 태그 ID"),
                                        fieldWithPath("data[].quote").type(STRING).description("의미 태그 문구"),
                                        fieldWithPath("data[].registeredCount").type(NUMBER).description("의미 태그 등록 수")
                                ).build())));
    }

    @DisplayName("모든 의미 태그를 조회한다.")
    @Test
    void getAllMeaningTags() throws Exception {

        MeaningTagElementResponse response_1 = MeaningTagDtoTestData.createMeaningTagElementResponse(1L, 20);
        MeaningTagElementResponse response_2 = MeaningTagDtoTestData.createMeaningTagElementResponse(2L, 10);

        given(meaningTagService.findAll())
                .willReturn(List.of(response_1, response_2));

        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/meaning-tags")
                .header("USER-ID", LOGIN_ID));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("전체 의미 태그 조회 결과입니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // document
        actions.andDo(document("find-all-meaning-tags",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("meaningTag")
                                .summary("전체 의미 태그 조회한다.")
                                .requestHeaders(
                                        headerWithName("USER-ID").description("사용자 ID")
                                )
                                .responseSchema(Schema.schema("find_all_meaning_tags_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(STRING).description("응답 상태"),
                                        fieldWithPath("message").type(STRING).description("응답 메시지"),
                                        fieldWithPath("data[].id").type(NUMBER).description("의미 태그 ID"),
                                        fieldWithPath("data[].quote").type(STRING).description("의미 태그 문구"),
                                        fieldWithPath("data[].registeredCount").type(NUMBER).description("의미 태그 등록 수")
                                ).build())));
    }
}