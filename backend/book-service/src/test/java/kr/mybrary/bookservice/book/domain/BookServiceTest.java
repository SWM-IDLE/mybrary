package kr.mybrary.bookservice.book.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import kr.mybrary.bookservice.book.BookDtoTestData;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.domain.dto.BookDtoMapper;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.request.BookDetailServiceRequest;
import kr.mybrary.bookservice.book.domain.e.BookAlreadyExistsException;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.repository.AuthorRepository;
import kr.mybrary.bookservice.book.persistence.repository.BookCategoryRepository;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import kr.mybrary.bookservice.book.persistence.repository.TranslatorRepository;
import kr.mybrary.bookservice.booksearch.BookSearchDtoTestData;
import kr.mybrary.bookservice.booksearch.domain.PlatformBookSearchApiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
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

    @Mock
    private BookCategoryRepository bookCategoryRepository;

    @Mock
    private PlatformBookSearchApiService platformBookSearchApiService;

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
        bookService.create(request);

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
                () -> assertThatThrownBy(() -> bookService.create(request)).isInstanceOf(BookAlreadyExistsException.class),
                () -> verify(bookRepository).findByIsbn10OrIsbn13(anyString(), anyString()),
                () -> verify(bookRepository).findByIsbn10OrIsbn13(anyString(), anyString()),
                () -> verify(authorRepository, never()).findByName(anyString()),
                () -> verify(translatorRepository, never()).findByName(anyString()),
                () -> verify(bookRepository, never()).save(any(Book.class))
        );
    }

    @Test
    @DisplayName("DB애서 ISBN을 통해서 도서 상세 정보를 가져온다.")
    void getBookByISBN() {

        // given
        BookDetailServiceRequest request = BookDtoTestData.createBookDetailServiceRequest();
        given(bookRepository.findByISBNWithAuthorAndCategoryUsingFetchJoin(anyString(), anyString()))
                .willReturn(Optional.of(BookFixture.COMMON_BOOK.getBook()));

        // when
        bookService.getBookDetailByISBN(request);

        // then
        assertAll(
                () -> verify(bookRepository, times(1)).findByISBNWithAuthorAndCategoryUsingFetchJoin(anyString(), anyString()),
                () -> verify(platformBookSearchApiService, never()).searchBookDetailWithISBN(any())
        );
    }

    @Test
    @DisplayName("DB애서 ISBN을 통해서 도서 상세 조회 시, 도서가 존재 하지 않으면 도서 API를 호출한다.")
    void getEmptyOptionalWhenBookNotExist() {

        // given
        BookDetailServiceRequest request = BookDtoTestData.createBookDetailServiceRequest();
        given(bookRepository.findByISBNWithAuthorAndCategoryUsingFetchJoin(anyString(), anyString()))
                .willReturn(Optional.empty());
        given(platformBookSearchApiService.searchBookDetailWithISBN(any())).willReturn(
                BookSearchDtoTestData.createBookSearchDetailResponse());

        // when
        bookService.getBookDetailByISBN(request);

        // then
        assertAll(
                () -> verify(bookRepository, times(1)).findByISBNWithAuthorAndCategoryUsingFetchJoin(anyString(), anyString()),
                () -> verify(platformBookSearchApiService, times(1)).searchBookDetailWithISBN(any())
        );
    }
}