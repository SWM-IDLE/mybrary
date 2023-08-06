package kr.mybrary.bookservice.book.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import kr.mybrary.bookservice.book.BookDtoTestData;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.domain.dto.request.BookDetailServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.response.BookDetailServiceResponse;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import kr.mybrary.bookservice.booksearch.BookSearchDtoTestData;
import kr.mybrary.bookservice.booksearch.domain.PlatformBookSearchApiService;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class BookReadServiceTest {

    @InjectMocks
    private BookReadService bookReadService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PlatformBookSearchApiService platformBookSearchApiService;

    @Mock
    private BookWriteService bookWriteService;


    @Test
    @DisplayName("DB애서 ISBN을 통해서 도서 상세 정보를 가져온다. + 로그인 유저가 해당 도서를 관심도서로 등록한 경우")
    void getBookByISBN() {

        // given
        BookDetailServiceRequest request = BookDtoTestData.createBookDetailServiceRequest();
        Book book = BookFixture.COMMON_BOOK.getBook();

        given(bookRepository.findByISBNWithAuthorAndCategoryUsingFetchJoin(anyString(), anyString()))
                .willReturn(Optional.of(book));

        // when
        BookDetailServiceResponse response = bookReadService.getBookDetailByISBN(request);

        // then
        assertAll(
                () -> assertThat(response.getTitle()).isEqualTo(book.getTitle()),
                () -> assertThat(response.getIsbn13()).isEqualTo(book.getIsbn13()),
                () -> assertThat(response.getAuthor()).isEqualTo(book.getAuthor()),
                () -> assertThat(response.isInterested()).isEqualTo(true),
                () -> verify(bookRepository, times(1)).findByISBNWithAuthorAndCategoryUsingFetchJoin(anyString(), anyString()),
                () -> verify(platformBookSearchApiService, never()).searchBookDetailWithISBN(any())
        );
    }

    @Test
    @DisplayName("DB애서 ISBN을 통해서 도서 상세 정보를 가져온다. + 로그인 유저가 해당 도서를 관심도서로 하지 않은 경우")
    void getBookByISBN2() {

        // given
        BookDetailServiceRequest request = BookDtoTestData.createBookDetailServiceRequest();
        Book book = BookFixture.COMMON_BOOK.getBookBuilder().bookInterests(List.of()).build();

        given(bookRepository.findByISBNWithAuthorAndCategoryUsingFetchJoin(anyString(), anyString()))
                .willReturn(Optional.of(book));

        // when
        BookDetailServiceResponse response = bookReadService.getBookDetailByISBN(request);

        // then
        assertAll(
                () -> assertThat(response.getTitle()).isEqualTo(book.getTitle()),
                () -> assertThat(response.getIsbn13()).isEqualTo(book.getIsbn13()),
                () -> assertThat(response.getAuthor()).isEqualTo(book.getAuthor()),
                () -> assertThat(response.isInterested()).isEqualTo(false),
                () -> verify(bookRepository, times(1)).findByISBNWithAuthorAndCategoryUsingFetchJoin(anyString(), anyString()),
                () -> verify(platformBookSearchApiService, never()).searchBookDetailWithISBN(any())
        );
    }



    @Test
    @DisplayName("DB애서 ISBN을 통해서 도서 상세 조회 시, 도서가 존재 하지 않으면 도서 API를 호출하고, 도서를 저장한다.")
    void getEmptyOptionalWhenBookNotExist() {

        // given
        BookDetailServiceRequest request = BookDtoTestData.createBookDetailServiceRequest();
        BookSearchDetailResponse bookSearchDetailResponse = BookSearchDtoTestData.createBookSearchDetailResponse();

        given(bookRepository.findByISBNWithAuthorAndCategoryUsingFetchJoin(anyString(), anyString()))
                .willReturn(Optional.empty());
        given(platformBookSearchApiService.searchBookDetailWithISBN(any())).willReturn(
                bookSearchDetailResponse);
        doNothing().when(bookWriteService).create(any());

        // when
        BookDetailServiceResponse response = bookReadService.getBookDetailByISBN(request);

        // then
        assertAll(
                () -> assertThat(response.getTitle()).isEqualTo(bookSearchDetailResponse.getTitle()),
                () -> assertThat(response.getIsbn13()).isEqualTo(bookSearchDetailResponse.getIsbn13()),
                () -> assertThat(response.getAuthor()).isEqualTo(bookSearchDetailResponse.getAuthor()),
                () -> assertThat(response.isInterested()).isEqualTo(false),
                () -> verify(bookRepository, times(1)).findByISBNWithAuthorAndCategoryUsingFetchJoin(anyString(), anyString()),
                () -> verify(platformBookSearchApiService, times(1)).searchBookDetailWithISBN(any()),
                () -> verify(bookWriteService, times(1)).create(any())
        );
    }
}