package kr.mybrary.userservice.authentication.domain.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.mybrary.userservice.global.util.JwtUtil;
import kr.mybrary.userservice.global.util.RedisUtil;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LogoutTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    RedisUtil redisUtil;
    ObjectMapper objectMapper = new ObjectMapper();

    static final String LOGIN_ID = "testId";
    static final String PASSWORD = "password123!";
    static final String SIGN_UP_URL = "/api/v1/users/sign-up";
    static final String LOGIN_URL = "/api/v1/auth/login";
    static final String LOGOUT_URL = "/api/v1/auth/logout";
    static final String ACCESS_TOKEN_HEADER = "Authorization";
    static final String REFRESH_TOKEN_HEADER = "Authorization-Refresh";
    static final String LOGOUT_FAIL_MESSAGE = "'로그아웃에 실패했습니다.'";
    private final String ACCESS_TOKEN_NOT_FOUND = "'로그아웃 처리할 액세스 토큰이 존재하지 않습니다.'";
    private final String INVALID_TOKEN = "'유효하지 않은 토큰입니다.'";
    private final String AUTHENTICATION_FAIL_STATUS = "401";

    static String accessToken;
    static String refreshToken;

//    @Test
//    @Order(1)
    @DisplayName("회원 가입 후 로그인하여 accessToken, refreshToken을 발급받는다.")
    void setUp() throws Exception {
        // 회원가입
        Map<String, String> signUpRequest = new HashMap<>();
        signUpRequest.put("loginId", LOGIN_ID);
        signUpRequest.put("password", PASSWORD);
        signUpRequest.put("nickname", "nickname");
        signUpRequest.put("email", "email@mail.com");

        mockMvc.perform(post(SIGN_UP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk());

        // 로그인
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("loginId", LOGIN_ID);
        loginRequest.put("password", PASSWORD);

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String token = result.getResponse().getHeader(ACCESS_TOKEN_HEADER);
                    assert token != null;
                })
                .andExpect(result -> {
                    String refreshToken = result.getResponse().getHeader(REFRESH_TOKEN_HEADER);
                    assert refreshToken != null;
                })
                .andDo(result -> {
                    accessToken = result.getResponse().getHeader(ACCESS_TOKEN_HEADER);
                    refreshToken = result.getResponse().getHeader(REFRESH_TOKEN_HEADER);
                });
    }

//    @Test
//    @Order(2)
    @DisplayName("발급된 accessToken, refreshToken으로 로그아웃을 요청하여 로그아웃에 성공한다.")
    void logoutSuccess() throws Exception {
        // given
        String requestAccessToken = "Bearer " + accessToken;
        String requestRefreshToken = "Bearer " + refreshToken;

        // when
        mockMvc.perform(post(LOGOUT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", requestAccessToken)
                        .header("Authorization-Refresh", requestRefreshToken))
                .andExpect(status().isOk());

        // then
        assert redisUtil.get(accessToken) != null;
    }

//    @Test
//    @Order(3)
    @DisplayName("로그아웃된 accessToken으로 인증을 요청하면 예외가 발생한다.")
    void loginWithLoggedOutToken() throws Exception {
        // given
        String requestAccessToken = "Bearer " + accessToken;

        // when
        mockMvc.perform(post("/auth/v1/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ACCESS_TOKEN_HEADER, requestAccessToken))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("{\"path\":\"\",\"error\":\"인증에 실패했습니다. 유효하지 않은 JWT 토큰입니다\",\"message\":\"로그아웃된 토큰입니다.\",\"status\":401}"));


        // then
    }

//    @Test
//    @Order(4)
    @DisplayName("토큰이 없는 상태로 로그아웃을 요청하면 예외가 발생한다.")
    void logoutWithoutToken() throws Exception {
        // given

        // when
        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("{'path':'','error':" + LOGOUT_FAIL_MESSAGE + ",'message':" +
                        ACCESS_TOKEN_NOT_FOUND + ",'status':" + AUTHENTICATION_FAIL_STATUS + "}"));

        // then
    }

//    @Test
//    @Order(5)
    @DisplayName("유효하지 않은 토큰으로 로그아웃을 요청하면 예외가 발생한다.")
    void logoutWithInvalidToken() throws Exception {
        // given
        String invalidToken = "Bearer invalid token";

        // when
        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ACCESS_TOKEN_HEADER, invalidToken))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("{'path':'','error':" + LOGOUT_FAIL_MESSAGE + ",'message':" +
                        INVALID_TOKEN + ",'status':" + AUTHENTICATION_FAIL_STATUS + "}"));

        // then
    }

//    @Test
//    @Order(6)
    @DisplayName("테스트 종료 후 테스트용 계정을 삭제한다.")
    void deleteTestUser() {
        userRepository.delete(userRepository.findByLoginId(LOGIN_ID).get());
    }
}
