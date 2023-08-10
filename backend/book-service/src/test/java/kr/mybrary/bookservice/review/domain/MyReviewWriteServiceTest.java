package kr.mybrary.bookservice.review.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import kr.mybrary.bookservice.mybook.MyBookFixture;
import kr.mybrary.bookservice.mybook.domain.MyBookService;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.MyBookReviewDtoTestData;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewCreateServiceRequest;
import kr.mybrary.bookservice.review.domain.exception.MyReviewAlreadyExistsException;
import kr.mybrary.bookservice.review.persistence.repository.MyReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MyReviewWriteServiceTest {

    @InjectMocks
    private MyReviewWriteService myReviewWriteService;

    @Mock
    private MyReviewRepository myBookReviewRepository;

    @Mock
    private MyBookService myBookService;

    @DisplayName("마이북 리뷰를 등록한다.")
    @Test
    void create() {

        // given
        MyReviewCreateServiceRequest request = MyBookReviewDtoTestData.createMyBookReviewCreateServiceRequest();
        MyBook myBook = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook();

        given(myBookService.findMyBookByIdWithBook(request.getMyBookId())).willReturn(myBook);
        given(myBookReviewRepository.existsByMyBook(myBook)).willReturn(false);
        given(myBookReviewRepository.save(any())).willReturn(any());

        // when
        myReviewWriteService.create(request);

        // then
        assertAll(
                () -> verify(myBookService, times(1)).findMyBookByIdWithBook(request.getMyBookId()),
                () -> verify(myBookReviewRepository, times(1)).existsByMyBook(myBook),
                () -> verify(myBookReviewRepository, times(1)).save(any())
        );
    }

    @DisplayName("마이북에 등록된 리뷰가 있으면 예외가 발생한다.")
    @Test
    void occurExceptionWhenExistMyBookReview() {

        // given
        MyReviewCreateServiceRequest request = MyBookReviewDtoTestData.createMyBookReviewCreateServiceRequest();
        MyBook myBook = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook();

        given(myBookService.findMyBookByIdWithBook(request.getMyBookId())).willReturn(myBook);
        given(myBookReviewRepository.existsByMyBook(myBook)).willReturn(true);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> myReviewWriteService.create(request))
                        .isInstanceOf(MyReviewAlreadyExistsException.class),
                () -> verify(myBookService, times(1)).findMyBookByIdWithBook(request.getMyBookId()),
                () -> verify(myBookReviewRepository, times(1)).existsByMyBook(myBook),
                () -> verify(myBookReviewRepository, never()).save(any())
        );
    }
}