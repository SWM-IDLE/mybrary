package kr.mybrary.userservice.authentication.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import jakarta.servlet.http.HttpServletResponse;
import kr.mybrary.userservice.authentication.domain.AuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser(username = "loginId_1", roles = {"USER"})
@AutoConfigureRestDocs
class AuthenticationControllerTest {

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;

    private final String BASE_URL = "/auth/v1";
    private final String STATUS_FIELD_DESCRIPTION = "응답 상태";
    private final String MESSAGE_FIELD_DESCRIPTION = "응답 메시지";

    @DisplayName("액세스 토큰과 리프레쉬 토큰을 재발급한다.")
    @Test
    void refreshToken() throws Exception {
        // given
        doAnswer(invocation -> {
            HttpServletResponse response = invocation.getArgument(1, HttpServletResponse.class);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setHeader("Authorization", "Bearer updatedAccessToken");
            response.setHeader("Authorization-Refresh", "Bearer updatedRefreshToken");
            return null;
        }).when(authenticationService).reIssueToken(any(), any(HttpServletResponse.class));

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.get(BASE_URL + "/refresh")
                        .with(csrf())
                        .header("Authorization", "Bearer accessToken")
                        .header("Authorization-Refresh", "Bearer refreshToken"));

        // then
        verify(authenticationService).reIssueToken(any(), any());

        actions.andExpect(status().is2xxSuccessful())
                        .andExpect(header().exists("Authorization"))
                        .andExpect(header().exists("Authorization-Refresh"));

        // docs
        actions.andDo(document("token-refresh",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("token-refresh")
                                .summary("액세스 토큰과 리프레쉬 토큰을 재발급한다.")
                                .requestSchema(Schema.schema("token_refresh_request"))
                                .requestHeaders(
                                        headerWithName("Authorization").description("액세스 토큰"),
                                        headerWithName("Authorization-Refresh").description("리프레쉬 토큰")
                                )
                                .responseSchema(Schema.schema("token_refresh_response"))
                                .responseHeaders(
                                        headerWithName("Authorization").description("재발급된 액세스 토큰"),
                                        headerWithName("Authorization-Refresh").description("재발급된 리프레쉬 토큰")
                                )
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                .description("응답 데이터").optional()
                                )
                                .build()
                ))
        );

    }
}