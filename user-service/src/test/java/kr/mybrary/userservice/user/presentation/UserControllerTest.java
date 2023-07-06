package kr.mybrary.userservice.user.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.mybrary.userservice.user.domain.UserService;
import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.userservice.user.domain.dto.response.ProfileServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.SignUpServiceResponse;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.presentation.dto.request.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser(username = "loginId_1", roles = {"USER"})
@AutoConfigureRestDocs
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("아이디, 비밀번호, 닉네임, 이메일을 입력해 회원 가입을 한다.")
    @Test
    void signUp() throws Exception {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId("loginId")
                .password("password123!")
                .nickname("nickname")
                .email("email@email.com")
                .build();

        SignUpServiceResponse signUpServiceResponse = SignUpServiceResponse.builder()
                .loginId(signUpRequest.getLoginId())
                .nickname(signUpRequest.getNickname())
                .email(signUpRequest.getEmail())
                .role(Role.USER)
                .build();

        given(userService.signUp(any(SignUpServiceRequest.class))).willReturn(signUpServiceResponse);

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/v1/users/sign-up")
                .with(csrf())
                .content(objectMapper.writeValueAsString(signUpRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value("201 CREATED"))
                .andExpect(jsonPath("$.message").value("회원 가입에 성공했습니다."))
                .andExpect(jsonPath("$.data.loginId").value(signUpRequest.getLoginId()))
                .andExpect(jsonPath("$.data.nickname").value(signUpRequest.getNickname()))
                .andExpect(jsonPath("$.data.email").value(signUpRequest.getEmail()))
                .andExpect(jsonPath("$.data.role").value(Role.USER.name()));

        // docs
        actions.andDo(document("user-sign-up",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-sign-up")
                                .summary("아이디, 비밀번호, 닉네임, 이메일을 입력해 회원 가입을 한다.")
                                .requestSchema(Schema.schema("user_sign_up_request_body"))
                                .requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원 가입 아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("회원 가입 비밀번호 (8~16자 영문 대 소문자, 숫자, 특수문자 사용)"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 가입 닉네임 (특수문자를 제외한 2~20자 사용)"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("회원 가입 이메일(선택)")
                                )
                                .responseSchema(Schema.schema("user_sign_up_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data.loginId").type(JsonFieldType.STRING).description("가입된 아이디"),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("가입된 닉네임"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("가입된 이메일"),
                                        fieldWithPath("data.role").type(JsonFieldType.STRING).description("가입된 권한")
                                )
                                .build()
                ))
        );
    }



    @DisplayName("로그인 된 사용자의 프로필 정보를 조회한다.")
    @Test
    void getProfile() throws Exception {
        // given
        ProfileServiceResponse profileServiceResponse = ProfileServiceResponse.builder()
                .nickname("nickname_1")
                .profileImageUrl("profileImageUrl_1")
                .email("email_1")
                .introduction("introduction_1")
                .build();

        given(userService.getProfile(anyString())).willReturn(profileServiceResponse);

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/users/profile")
                .with(csrf())
                .header("USER-ID", "loginId_1"));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("로그인 된 사용자의 프로필 정보입니다."))
                .andExpect(jsonPath("$.data.nickname").value(profileServiceResponse.getNickname()))
                .andExpect(jsonPath("$.data.profileImageUrl").value(profileServiceResponse.getProfileImageUrl()))
                .andExpect(jsonPath("$.data.email").value(profileServiceResponse.getEmail()))
                .andExpect(jsonPath("$.data.introduction").value(profileServiceResponse.getIntroduction()));

        verify(userService).getProfile(anyString());

        // docs
        actions.andDo(document("get-user-profile",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-profile")
                                .summary("로그인한 사용자의 프로필 정보를 조회한다.")
                                .requestSchema(Schema.schema("get_user_profile_request_body"))
                                .requestHeaders(
                                        headerWithName("USER-ID").description("로그인 할 사용자의 아이디")
                                )
                                .responseSchema(Schema.schema("get_user_profile_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("사용자의 닉네임"),
                                        fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING).description("사용자의 프로필 이미지 URL"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("사용자의 이메일"),
                                        fieldWithPath("data.introduction").type(JsonFieldType.STRING).description("사용자의 한 줄 소개")
                                )
                                .build()
                ))
        );
    }

}