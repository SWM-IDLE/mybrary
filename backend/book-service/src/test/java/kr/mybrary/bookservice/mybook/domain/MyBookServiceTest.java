package kr.mybrary.bookservice.mybook.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.book.domain.BookService;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookCreateServiceRequest;
import kr.mybrary.bookservice.mybook.domain.exception.MyBookAlreadyExistsException;
import kr.mybrary.bookservice.mybook.persistence.repository.MyBookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MyBookServiceTest {

    @InjectMocks
    private MyBookService myBookService;

    @Mock
    private MyBookRepository myBookRepository;

    @Mock
    private BookService bookService;

    @DisplayName("도서를 마이북으로 등록한다.")
    @Test
    void registerMyBook() {

        // given
        MyBookCreateServiceRequest request = createMyBookCreateServiceRequest();


        given(bookService.getRegisteredOrNewBook(any()))
                .willReturn(Book.builder().id(1L).build());
        given(myBookRepository.existsByUserIdAndBook(any(), any())).willReturn(false);
        given(myBookRepository.save(any())).willReturn(any());

        // when
        myBookService.create(request);

        // then
        assertAll(
                () -> verify(bookService).getRegisteredOrNewBook(any()),
                () -> verify(myBookRepository).existsByUserIdAndBook(any(), any()),
                () -> verify(myBookRepository).save(any())
        );
    }

    @DisplayName("기존에 마이북으로 설정한 도서를 마이북으로 등록하면 예외가 발생한다.")
    @Test
    void occurExceptionWhenRegisterDuplicatedBook() {

        // given
        MyBookCreateServiceRequest request = createMyBookCreateServiceRequest();

        given(bookService.getRegisteredOrNewBook(any()))
                .willReturn(Book.builder().id(1L).build());
        given(myBookRepository.existsByUserIdAndBook(any(), any())).willReturn(true);

        // when, then
        assertThrows(MyBookAlreadyExistsException.class, () -> myBookService.create(request));

        assertAll(
                () -> verify(bookService).getRegisteredOrNewBook(any()),
                () -> verify(myBookRepository).existsByUserIdAndBook(any(), any()),
                () -> verify(myBookRepository, never()).save(any())
        );
    }

    private static MyBookCreateServiceRequest createMyBookCreateServiceRequest() {
        return MyBookCreateServiceRequest.builder()
                .userId("test1")
                .title("title")
                .description("description")
                .isbn10("isbn10")
                .isbn13("isbn13")
                .publisher("publisher")
                .publicationDate(LocalDateTime.now())
                .price(10000)
                .thumbnailUrl("thumbnailUrl")
                .authors(List.of("author1", "author2"))
                .translators(List.of("translator1", "translator2"))
                .build();
    }
}