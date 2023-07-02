package kr.mybrary.bookservice.book.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.book.domain.BookService;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.presentation.dto.request.BookCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class)
@MockBean(JpaMetamodelMappingContext.class)
class BookControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @DisplayName("새로운 도서를 등록한다.")
    @Test
    void create() throws Exception {

        // given
        BookCreateRequest request = createBookCreateRequest();

        String requestJson = objectMapper.writeValueAsString(request);

        given(bookService.getRegisteredBook(request.toServiceRequest()))
                .willReturn(any(Book.class));

        // when, then
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("201 CREATED"))
                .andExpect(jsonPath("$.message").value("도서 등록에 성공했습니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    private BookCreateRequest createBookCreateRequest() {
        return BookCreateRequest.builder()
                .title("title")
                .description("description")
                .detailsUrl("detailUrl")
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