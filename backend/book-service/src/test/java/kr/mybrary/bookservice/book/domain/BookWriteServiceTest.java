package kr.mybrary.bookservice.book.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import kr.mybrary.bookservice.book.BookDtoTestData;
import kr.mybrary.bookservice.book.domain.dto.BookDtoMapper;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import kr.mybrary.bookservice.book.domain.exception.BookAlreadyExistsException;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.repository.AuthorRepository;
import kr.mybrary.bookservice.book.persistence.repository.BookCategoryRepository;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import kr.mybrary.bookservice.book.persistence.repository.TranslatorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class BookWriteServiceTest {

    @InjectMocks
    private BookWriteService bookWriteService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private TranslatorRepository translatorRepository;

    @Mock
    private BookCategoryRepository bookCategoryRepository;

    @Test
    @DisplayName("새로운 도서를 저장한다.")
    void getNewBookWhenBookNotExist() {

        // given
        BookCreateServiceRequest request = BookDtoTestData.createBookCreateServiceRequest();
        Book book = BookDtoMapper.INSTANCE.bookCreateRequestToEntity(request);

        given(bookRepository.findByIsbn10OrIsbn13(request.getIsbn10(), request.getIsbn13()))
                .willReturn(Optional.empty());
        given(bookRepository.save(any(Book.class))).willReturn(book);
        given(authorRepository.findByAid(anyInt())).willReturn(Optional.empty());
        given(translatorRepository.findByTid(anyInt())).willReturn(Optional.empty());
        given(bookCategoryRepository.findByCid(anyInt())).willReturn(Optional.empty());

        // when
        bookWriteService.create(request);

        // then
        assertAll(
                () -> verify(bookRepository).findByIsbn10OrIsbn13(anyString(), anyString()),
                () -> verify(authorRepository, times(2)).findByAid(anyInt()),
                () -> verify(translatorRepository, times(2)).findByTid(anyInt()),
                () -> verify(bookCategoryRepository, times(1)).findByCid(anyInt()),
                () -> verify(bookRepository).save(any(Book.class))
        );
    }

    @Test
    @DisplayName("도서가 이미 등록되어 있을 경우, 예외를 발생시킨다.")
    void getRegisteredBookWhenBookExist() {

        // given
        BookCreateServiceRequest request = BookDtoTestData.createBookCreateServiceRequest();
        Book book = BookDtoMapper.INSTANCE.bookCreateRequestToEntity(request);

        given(bookRepository.findByIsbn10OrIsbn13(request.getIsbn10(), request.getIsbn13()))
                .willReturn(Optional.of(book));

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> bookWriteService.create(request)).isInstanceOf(BookAlreadyExistsException.class),
                () -> verify(bookRepository).findByIsbn10OrIsbn13(anyString(), anyString()),
                () -> verify(bookRepository).findByIsbn10OrIsbn13(anyString(), anyString()),
                () -> verify(authorRepository, never()).findByName(anyString()),
                () -> verify(translatorRepository, never()).findByName(anyString()),
                () -> verify(bookRepository, never()).save(any(Book.class))
        );
    }
}
