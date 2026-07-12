package kr.mybrary.global.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

class GlobalExceptionHandlerTest {

    @RestController
    static class TestController {
        @GetMapping("/test/application-exception")
        void throwApplicationException() {
            throw new ApplicationException(404, "US-01", "사용자를 찾을 수 없습니다.");
        }

        @GetMapping("/test/runtime-exception")
        void throwRuntimeException() {
            throw new RuntimeException("예기치 못한 오류");
        }
    }

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("ApplicationException이 발생하면 해당 상태코드와 에러 코드를 반환한다.")
    void applicationException_returnsCorrectStatusAndErrorCode() throws Exception {
        mockMvc.perform(get("/test/application-exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("US-01"))
                .andExpect(jsonPath("$.errorMessage").value("사용자를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("RuntimeException이 발생하면 400 Bad Request와 에러 코드 RT-01을 반환한다.")
    void runtimeException_returns400WithErrorCodeRT01() throws Exception {
        mockMvc.perform(get("/test/runtime-exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("RT-01"))
                .andExpect(jsonPath("$.errorMessage").value("예기치 못한 오류"));
    }
}
