package kr.mybrary.bookservice.book.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.mybrary.bookservice.book.domain.dto.BookDtoMapper;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.repository.AuthorRepository;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import kr.mybrary.bookservice.book.persistence.repository.TranslatorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private TranslatorRepository translatorRepository;

    @Test
    @DisplayName("새로운 도서를 저장한다.")
    void getNewBookWhenBookNotExist() {

        // given
        BookCreateServiceRequest request = createBookCreateServiceRequest();
        Book book = BookDtoMapper.INSTANCE.bookCreateRequestToEntity(request);

        given(bookRepository.findByIsbn10OrIsbn13(request.getIsbn10(), request.getIsbn10()))
                .willReturn(Optional.empty());
        given(bookRepository.save(any(Book.class))).willReturn(book);
        given(authorRepository.findByName(anyString())).willReturn(Optional.empty());
        given(translatorRepository.findByName(anyString())).willReturn(Optional.empty());

        // when
        bookService.getRegisteredOrNewBook(request);

        // then
        assertAll(
                () -> verify(bookRepository).findByIsbn10OrIsbn13(anyString(), anyString()),
                () -> verify(authorRepository, times(2)).findByName(anyString()),
                () -> verify(translatorRepository, times(2)).findByName(anyString()),
                () -> verify(bookRepository).save(any(Book.class))
        );
    }

    @Test
    @DisplayName("도서가 이미 등록되어 있을 경우, 기존 도서를 가져온다.")
    void getRegisteredBookWhenBookExist() {

        // given
        BookCreateServiceRequest request = createBookCreateServiceRequest();
        Book book = BookDtoMapper.INSTANCE.bookCreateRequestToEntity(request);

        given(bookRepository.findByIsbn10OrIsbn13(request.getIsbn10(), request.getIsbn10()))
                .willReturn(Optional.of(book));

        // when
        bookService.getRegisteredOrNewBook(request);

        // then
        assertAll(
                () -> verify(bookRepository).findByIsbn10OrIsbn13(anyString(), anyString()),
                () -> verify(authorRepository, never()).findByName(anyString()),
                () -> verify(translatorRepository, never()).findByName(anyString()),
                () -> verify(bookRepository, never()).save(any(Book.class))
        );
    }

    private BookCreateServiceRequest createBookCreateServiceRequest() {
        return BookCreateServiceRequest.builder()
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