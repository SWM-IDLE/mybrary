package kr.mybrary.userservice.authentication.domain.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import kr.mybrary.userservice.global.util.JwtUtil;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
public class LoginTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    ObjectMapper objectMapper = new ObjectMapper();

    static final String LOGIN_ID = "testId";
    static final String PASSWORD = "password123!";
    static final String LOGIN_URL = "/api/v1/auth/login";
    static final String CONTENT_TYPE_JSON = "application/json";

//    @BeforeEach
    void setUp() throws Exception {
        // 회원가입
        Map<String, String> signUpRequest = new HashMap<>();
        signUpRequest.put("loginId", LOGIN_ID);
        signUpRequest.put("password", PASSWORD);
        signUpRequest.put("nickname", "nickname");
        signUpRequest.put("email", "email@mail.com");

        mockMvc.perform(post("/api/v1/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk());
    }

//    @AfterEach
    void tearDown() {
        userRepository.delete(userRepository.findByLoginId(LOGIN_ID).get());
    }

    @DisplayName("로그인 요청 시 로그인 성공")
//    @Test
    void loginSuccess() throws Exception {
        // given
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("loginId", LOGIN_ID);
        loginRequest.put("password", PASSWORD);

        // when // then
        MvcResult result = mockMvc.perform(post(LOGIN_URL)
                        .contentType(CONTENT_TYPE_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().exists("Authorization-Refresh"))
                .andReturn();

        // JWT 토큰의 payload에서 아이디 검증
        String accessToken = result.getResponse().getHeader("Authorization");
        jwtUtil.getLoginIdFromValidAccessToken(accessToken)
                .ifPresent(loginId -> assertThat(loginId).isEqualTo(LOGIN_ID));
    }

    @DisplayName("로그인 요청 시 비밀번호가 틀리면 로그인 실패")
//    @Test
    void loginWithWrongPassword() throws Exception {
        // given
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("loginId", LOGIN_ID);
        loginRequest.put("password", "wrongPassword");

        // when // then
        mockMvc.perform(post(LOGIN_URL)
                        .contentType(CONTENT_TYPE_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("Authorization-Refresh"))
                .andExpect(
                        content().json("{'errorCode':'U-06','errorMessage':'비밀번호가 일치하지 않습니다.'}"));
    }

    @DisplayName("로그인 요청 시 없는 아이디라면 로그인 실패")
//    @Test
    void loginWithWrongLoginId() throws Exception {
        // given
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("loginId", "wrongId");
        loginRequest.put("password", PASSWORD);

        // when // then
        mockMvc.perform(post(LOGIN_URL)
                        .contentType(CONTENT_TYPE_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("Authorization-Refresh"))
                .andExpect(content().json("{'errorCode':'U-05','errorMessage':'존재하지 않는 아이디입니다: wrongId'}"));
    }

    @DisplayName("로그인 요청 시 Json 형식이 아니면 로그인 실패")
//    @Test
    void loginWithWrongFormat() throws Exception {
        // given
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("loginId", LOGIN_ID);
        loginRequest.put("password", PASSWORD);

        // when // then
        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("Authorization-Refresh"))
                .andExpect(content().json(
                        "{'errorCode':'U-07','errorMessage':'지원되지 않는 Content-Type 입니다: text/plain. JSON 형식으로 요청해주세요.'}"));
    }

}
