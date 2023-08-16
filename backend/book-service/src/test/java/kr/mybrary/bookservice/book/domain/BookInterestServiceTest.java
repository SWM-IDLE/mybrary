package kr.mybrary.bookservice.book.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.never;

import java.util.List;
import java.util.Optional;
import kr.mybrary.bookservice.book.BookDtoTestData;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.BookInterestFixture;
import kr.mybrary.bookservice.book.domain.dto.request.BookInterestServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.request.BookInterestStatusServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.request.BookMyInterestFindServiceRequest;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.BookInterest;
import kr.mybrary.bookservice.book.persistence.BookOrderType;
import kr.mybrary.bookservice.book.persistence.repository.BookInterestRepository;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestElementResponse;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestHandleResponse;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestStatusResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class BookInterestServiceTest {

    @InjectMocks
    private BookInterestService bookInterestService;

    @Mock
    private BookInterestRepository bookInterestRepository;

    @Mock
    private BookReadService bookReadService;

    @DisplayName("관심 도서로 등록한다.")
    @Test
    void registerBookInterest() {

        // given
        BookInterestServiceRequest serviceRequest = BookDtoTestData.createBookInterestServiceRequest();

        Book book = BookFixture.COMMON_BOOK.getBook();
        int originalBookInterestCount = book.getInterestCount();

        given(bookReadService.getRegisteredBookByISBN13(anyString())).willReturn(book);
        given(bookInterestRepository.findByBookAndUserId(any(Book.class), anyString())).willReturn(Optional.empty());
        given(bookInterestRepository.save(any(BookInterest.class))).willReturn(any());

        // when
        BookInterestHandleResponse response = bookInterestService.handleBookInterest(serviceRequest);

        // then
        assertAll(
                () -> verify(bookReadService, times(1)).getRegisteredBookByISBN13(anyString()),
                () -> verify(bookInterestRepository, times(1)).findByBookAndUserId(any(Book.class), anyString()),
                () -> verify(bookInterestRepository, times(1)).save(any(BookInterest.class)),
                () -> assertThat(response.isInterested()).isTrue(),
                () -> assertThat(book.getInterestCount()).isEqualTo(originalBookInterestCount + 1)
        );
    }

    @DisplayName("관심 도서 설정을 취소한다.")
    @Test
    void cancelBookInterest() {

        // given
        BookInterestServiceRequest serviceRequest = BookDtoTestData.createBookInterestServiceRequest();

        Book book = BookFixture.COMMON_BOOK.getBook();
        BookInterest bookInterest = BookInterestFixture.COMMON_BOOK_INTEREST.getBookInterestBuilder()
                .userId(serviceRequest.getLoginId())
                .book(book).build();

        int originalBookInterestCount = book.getInterestCount();

        given(bookReadService.getRegisteredBookByISBN13(anyString())).willReturn(book);
        given(bookInterestRepository.findByBookAndUserId(any(Book.class), anyString())).willReturn(
                Optional.of(bookInterest));
        doNothing().when(bookInterestRepository).delete(any(BookInterest.class));

        // when
        BookInterestHandleResponse response = bookInterestService.handleBookInterest(serviceRequest);

        // then
        assertAll(
                () -> verify(bookReadService, times(1)).getRegisteredBookByISBN13(anyString()),
                () -> verify(bookInterestRepository, times(1)).findByBookAndUserId(any(Book.class), anyString()),
                () -> verify(bookInterestRepository, times(1)).delete(any(BookInterest.class)),
                () -> assertThat(response.isInterested()).isFalse(),
                () -> assertThat(book.getInterestCount()).isEqualTo(originalBookInterestCount - 1)
        );
    }

    @DisplayName("내 관심 도서 목록을 조회한다.")
    @Test
    void getBookInterestList() {

        // given
        BookMyInterestFindServiceRequest serviceRequest = BookDtoTestData.createBookMyInterestFindServiceRequest();
        List<BookInterest> bookInterestList = List.of(BookInterestFixture.COMMON_BOOK_INTEREST.getBookInterest());

        given(bookInterestRepository.findAllByUserIdWithBook(any(), any(BookOrderType.class))).willReturn(bookInterestList);

        // when
        List<BookInterestElementResponse> responses = bookInterestService.getBookInterestList(serviceRequest);

        // then
        assertAll(
                () -> verify(bookInterestRepository, times(1)).findAllByUserIdWithBook(any(), any(BookOrderType.class)),
                () -> assertThat(responses.size()).isEqualTo(1)
        );
    }

    @DisplayName("로그인 유저가 해당 도서를 관심 도서로 등록했는지 확인한다.")
    @Test
    void isLoginUserRegisterInterestThisBook() {

        // given
        BookInterestStatusServiceRequest request = BookDtoTestData.createBookInterestStatusServiceRequest();
        Book book = BookFixture.COMMON_BOOK.getBook();

        given(bookReadService.findOptionalBookByISBN13(request.getIsbn13())).willReturn(Optional.of(book));
        given(bookInterestRepository.existsByBookAndUserId(book, request.getLoginId())).willReturn(true);

        // when
        BookInterestStatusResponse response = bookInterestService.getInterestStatus(request);

        // then
        assertAll(
                () -> verify(bookReadService, times(1)).findOptionalBookByISBN13(anyString()),
                () -> verify(bookInterestRepository, times(1)).existsByBookAndUserId(any(Book.class), anyString()),
                () -> assertThat(response.isInterested()).isTrue()
        );
    }

    @DisplayName("로그인 유저가 해당 도서를 관심 도서로 등록했는지 확인시, 도서가 존재하지 않으면 false를 반환한다.")
    @Test
    void returnFalseResponseWhenBookIsNotExisted() {

        // given
        BookInterestStatusServiceRequest request = BookDtoTestData.createBookInterestStatusServiceRequest();

        given(bookReadService.findOptionalBookByISBN13(request.getIsbn13())).willReturn(Optional.empty());

        // when
        BookInterestStatusResponse response = bookInterestService.getInterestStatus(request);

        // then
        assertAll(
                () -> verify(bookReadService, times(1)).findOptionalBookByISBN13(anyString()),
                () -> verify(bookInterestRepository, never()).existsByBookAndUserId(any(Book.class), anyString()),
                () -> assertThat(response.isInterested()).isFalse()
        );
    }
}