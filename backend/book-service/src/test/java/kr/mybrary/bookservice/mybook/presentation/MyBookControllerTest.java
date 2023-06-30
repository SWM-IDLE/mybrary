package kr.mybrary.bookservice.mybook.presentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.book.domain.BookService;
import kr.mybrary.bookservice.mybook.domain.MyBookService;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookCreateServiceRequest;
import kr.mybrary.bookservice.mybook.presentation.dto.request.MyBookCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MyBookController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MyBookControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyBookService myBookService;

    @DisplayName("내 서재에 책을 추가한다.")
    @Test
    void assignMyBook() throws Exception {
        // given
        MyBookCreateRequest request = createMyBookCreateRequest();

        String requestJson = objectMapper.writeValueAsString(request);

        given(myBookService.create(request.toServiceRequest("userId"))).willReturn(any());

        // when then
        mockMvc.perform(post("/mybooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());
    }

    private MyBookCreateRequest createMyBookCreateRequest() {
        return MyBookCreateRequest.builder()
                .title("title")
                .description("description")
                .isbn10("isbn10")
                .isbn13("isbn13")
                .publisher("publisher")
                .price(10000)
                .publicationDate(LocalDateTime.now())
                .translators(List.of("translator1", "translator2"))
                .authors(List.of("author1", "author2"))
                .thumbnailUrl("thumbnailUrl")
                .build();
    }
}