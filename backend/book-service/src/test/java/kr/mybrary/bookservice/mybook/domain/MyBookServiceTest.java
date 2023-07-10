package kr.mybrary.bookservice.mybook.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import kr.mybrary.bookservice.book.domain.BookService;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.MyBookFixture;
import kr.mybrary.bookservice.mybook.MybookDtoTestData;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookCreateServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookDetailServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookFindAllServiceRequest;
import kr.mybrary.bookservice.mybook.domain.exception.MyBookAccessDeniedException;
import kr.mybrary.bookservice.mybook.domain.exception.MyBookAlreadyExistsException;
import kr.mybrary.bookservice.mybook.domain.exception.MyBookNotFoundException;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.mybook.persistence.repository.MyBookRepository;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookDetailResponse;
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

    private static final String LOGIN_ID = "LOGIN_USER_ID";
    private static final String OTHER_USER_ID = "OTHER_USER_ID";

    @DisplayName("도서를 마이북으로 등록한다.")
    @Test
    void registerMyBook() {

        // given
        MyBookCreateServiceRequest request = MybookDtoTestData.createMyBookCreateServiceRequest();

        given(bookService.getRegisteredBook(any()))
                .willReturn(Book.builder().id(1L).build());
        given(myBookRepository.existsByUserIdAndBook(any(), any())).willReturn(false);
        given(myBookRepository.save(any())).willReturn(any());

        // when
        myBookService.create(request);

        // then
        assertAll(
                () -> verify(bookService).getRegisteredBook(any()),
                () -> verify(myBookRepository).existsByUserIdAndBook(any(), any()),
                () -> verify(myBookRepository).save(any())
        );
    }

    @DisplayName("기존에 마이북으로 설정한 도서를 마이북으로 등록하면 예외가 발생한다.")
    @Test
    void occurExceptionWhenRegisterDuplicatedBook() {

        // given
        MyBookCreateServiceRequest request = MybookDtoTestData.createMyBookCreateServiceRequest();

        given(bookService.getRegisteredBook(any()))
                .willReturn(Book.builder().id(1L).build());
        given(myBookRepository.existsByUserIdAndBook(any(), any())).willReturn(true);

        // when, then
        assertThrows(MyBookAlreadyExistsException.class, () -> myBookService.create(request));

        assertAll(
                () -> verify(bookService).getRegisteredBook(any()),
                () -> verify(myBookRepository).existsByUserIdAndBook(any(), any()),
                () -> verify(myBookRepository, never()).save(any())
        );
    }

    @DisplayName("나의 마이북을 모두 조회시, 비공개 설정된 마이북도 모두 조회한다.")
    @Test
    void findAllMyBooks() {

        //given
        MyBookFindAllServiceRequest request = MybookDtoTestData.createMyBookFindAllServiceRequest(
                LOGIN_ID, LOGIN_ID);

        given(myBookRepository.findAllByUserId(any())).willReturn(
                List.of(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook(),
                        MyBookFixture.NOT_SHOWABLE_LOGIN_USER_MYBOOK.getMyBook()));

        // when, then
        assertAll(
                () -> assertThat(myBookService.findAllMyBooks(request).size()).isEqualTo(2),
                () -> verify(myBookRepository).findAllByUserId(request.getUserId())
        );
    }

    @DisplayName("다른 유저의 마이북을 모두 조회시, 비공개 설정된 마이북은 조회되지 않는다.")
    @Test
    void findOtherUserAllMyBooks() {

        //given
        MyBookFindAllServiceRequest request = MybookDtoTestData.createMyBookFindAllServiceRequest(
                OTHER_USER_ID, LOGIN_ID);

        given(myBookRepository.findAllByUserId(any())).willReturn(
                List.of(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook(),
                        MyBookFixture.NOT_SHOWABLE_OTHER_USER_MYBOOK.getMyBook()));

        // when, then
        assertAll(
                () -> assertThat(myBookService.findAllMyBooks(request).size()).isEqualTo(1),
                () -> verify(myBookRepository).findAllByUserId(request.getUserId())
        );
    }

    @DisplayName("마이북 조회시, 마이북에서 삭제한 도서는 조회되지 않는다.")
    @Test
    void findAllMyBooksWithoutDeletedMyBook() {

        //given
        MyBookFindAllServiceRequest request = MybookDtoTestData.createMyBookFindAllServiceRequest(
                LOGIN_ID, LOGIN_ID);

        given(myBookRepository.findAllByUserId(any())).willReturn(
                List.of(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook(),
                        MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook(),
                        MyBookFixture.DELETED_LOGIN_USER_MYBOOK.getMyBook()));

        // when, then
        assertAll(
                () -> assertThat(myBookService.findAllMyBooks(request).size()).isEqualTo(2),
                () -> verify(myBookRepository).findAllByUserId(request.getUserId())
        );
    }

    @DisplayName("내 마이북 상세보기한다.")
    @Test
    void findMyBookDetail() {

        //given
        MyBookDetailServiceRequest request = MyBookDetailServiceRequest.of(LOGIN_ID, 1L);
        MyBook myBook = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook();

        given(myBookRepository.findByIdAndDeletedIsFalse(any())).willReturn(
                Optional.ofNullable(myBook));

        // when
        MyBookDetailResponse myBookDetail = myBookService.findMyBookDetail(request);

        // then
        assertAll(
                () -> assertThat(myBookDetail).isInstanceOf(MyBookDetailResponse.class),
                () -> assertThat(myBookDetail.getId()).isEqualTo(myBook.getId()),
                () -> verify(myBookRepository).findByIdAndDeletedIsFalse(request.getMybookId())
        );
    }

    @DisplayName("마이북 ID에 해당하는 마이북이 없으면 예외가 발생한다.")
    @Test
    void occurExceptionWhenFindMyBookDetailWithNotExistMyBook() {

        //given
        MyBookDetailServiceRequest request = MyBookDetailServiceRequest.of(LOGIN_ID, 1L);

        given(myBookRepository.findByIdAndDeletedIsFalse(any())).willReturn(
                Optional.empty());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> myBookService.findMyBookDetail(request))
                        .isInstanceOf(MyBookNotFoundException.class),
                () -> verify(myBookRepository).findByIdAndDeletedIsFalse(request.getMybookId())
        );
    }

    @DisplayName("다른 사람의 비공개 마이북 상세보기를 요청하면 예외가 발생한다.")
    @Test
    void occurExceptionWhenFindOtherUserPrivateMyBookDetail() {

        //given
        MyBookDetailServiceRequest request = MyBookDetailServiceRequest.of(LOGIN_ID, 1L);
        MyBook myBook = MyBookFixture.NOT_SHOWABLE_OTHER_USER_MYBOOK.getMyBook();

        given(myBookRepository.findByIdAndDeletedIsFalse(any())).willReturn(
                Optional.ofNullable(myBook));

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> myBookService.findMyBookDetail(request))
                        .isInstanceOf(MyBookAccessDeniedException.class),
                () -> verify(myBookRepository).findByIdAndDeletedIsFalse(request.getMybookId())
        );
    }
}