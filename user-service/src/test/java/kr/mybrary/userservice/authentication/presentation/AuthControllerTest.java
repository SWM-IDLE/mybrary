package kr.mybrary.userservice.authentication.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.mybrary.userservice.authentication.domain.AuthenticationService;
import kr.mybrary.userservice.authentication.presentation.dto.request.SignUpRequest;
import kr.mybrary.userservice.authentication.presentation.dto.response.SignUpResponse;
import kr.mybrary.userservice.global.ControllerTestConfig;
import kr.mybrary.userservice.user.persistence.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser(username = "guest", roles = {"GUEST"})
class AuthControllerTest extends ControllerTestConfig {

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("로그인 아이디, 비밀번호, 닉네임, 이메일로 회원가입을 요청하면 회원가입에 성공한다.")
    @Test
    void signUp() throws Exception {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId("loginId")
                .password("password123!")
                .nickname("nickname")
                .email("email@email.com")
                .build();

        SignUpResponse signUpResponse = SignUpResponse.builder()
                .loginId(signUpRequest.getLoginId())
                .nickname(signUpRequest.getNickname())
                .email(signUpRequest.getEmail())
                .role(Role.USER)
                .build();

        given(authenticationService.signUp(any(SignUpRequest.class))).willReturn(signUpResponse);


        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/auth/sign-up")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value("201 CREATED"))
                .andExpect(jsonPath("$.message").value("회원 가입에 성공했습니다."))
                .andExpect(jsonPath("$.data.loginId").value(signUpRequest.getLoginId()))
                .andExpect(jsonPath("$.data.nickname").value(signUpRequest.getNickname()))
                .andExpect(jsonPath("$.data.email").value(signUpRequest.getEmail()))
                .andExpect(jsonPath("$.data.role").value(Role.USER.name()));

        verify(authenticationService).signUp(any(SignUpRequest.class));
    }

}