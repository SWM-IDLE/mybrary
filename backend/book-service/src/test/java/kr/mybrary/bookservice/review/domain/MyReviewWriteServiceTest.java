package kr.mybrary.bookservice.review.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import java.util.Optional;
import kr.mybrary.bookservice.mybook.MyBookFixture;
import kr.mybrary.bookservice.mybook.domain.MyBookService;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.MyReviewDtoTestData;
import kr.mybrary.bookservice.review.MyReviewFixture;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewCreateServiceRequest;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewDeleteServiceRequest;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewUpdateServiceRequest;
import kr.mybrary.bookservice.review.domain.exception.MyReviewAccessDeniedException;
import kr.mybrary.bookservice.review.domain.exception.MyReviewAlreadyExistsException;
import kr.mybrary.bookservice.review.persistence.MyReview;
import kr.mybrary.bookservice.review.persistence.repository.MyReviewRepository;
import kr.mybrary.bookservice.review.presentation.dto.response.MyReviewUpdateResponse;
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

    @DisplayName("마이북 리뷰를 등록한다. 리뷰 등록시 Book의 리뷰 수와 별점이 변경된다.")
    @Test
    void create() {

        // given
        MyReviewCreateServiceRequest request = MyReviewDtoTestData.createMyBookReviewCreateServiceRequest();
        MyBook myBook = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook();
        Integer originReviewCount = myBook.getBook().getReviewCount();
        Double originStarRating = myBook.getBook().getStarRating();

        given(myBookService.findMyBookByIdWithBook(request.getMyBookId())).willReturn(myBook);
        given(myBookReviewRepository.existsByMyBook(myBook)).willReturn(false);
        given(myBookReviewRepository.save(any())).willReturn(any());

        // when
        myReviewWriteService.create(request);

        // then
        assertAll(
                () -> assertThat(myBook.getBook().getReviewCount()).isEqualTo(originReviewCount + 1),
                () -> assertThat(myBook.getBook().getStarRating()).isEqualTo(originStarRating + request.getStarRating()),
                () -> verify(myBookService, times(1)).findMyBookByIdWithBook(request.getMyBookId()),
                () -> verify(myBookReviewRepository, times(1)).existsByMyBook(myBook),
                () -> verify(myBookReviewRepository, times(1)).save(any())
        );
    }

    @DisplayName("다른 유저의 마이북 리뷰를 등록할 시, 예외가 발생한다.")
    @Test
    void occurExceptionWhenWriteOtherBookReview() {

        // given
        MyReviewCreateServiceRequest request = MyReviewDtoTestData.createMyBookReviewCreateServiceRequest();
        MyBook myBook = MyBookFixture.COMMON_OTHER_USER_MYBOOK.getMyBook();

        given(myBookService.findMyBookByIdWithBook(request.getMyBookId())).willReturn(myBook);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> myReviewWriteService.create(request))
                        .isInstanceOf(MyReviewAccessDeniedException.class),
                () -> verify(myBookService, times(1)).findMyBookByIdWithBook(request.getMyBookId()),
                () -> verify(myBookReviewRepository, never()).existsByMyBook(myBook),
                () -> verify(myBookReviewRepository, never()).save(any())
        );
    }

    @DisplayName("마이북에 등록된 리뷰가 있으면 예외가 발생한다.")
    @Test
    void occurExceptionWhenExistMyBookReview() {

        // given
        MyReviewCreateServiceRequest request = MyReviewDtoTestData.createMyBookReviewCreateServiceRequest();
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

    @DisplayName("마이 리뷰를 수정한다. 수정시 기존 Book의 별점도 수정된다.")
    @Test
    void updateMyReview() {

        // given
        MyReview myReview = MyReviewFixture.COMMON_MY_BOOK_REVIEW.getMyBookReviewBuilder()
                .book(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook().getBook())
                .myBook(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook()).build();
        MyReviewUpdateServiceRequest request = MyReviewDtoTestData.createMyReviewUpdateServiceRequest(
                myReview.getMyBook().getUserId(), myReview.getId());

        Double originBookStarRating = myReview.getBook().getStarRating();
        Double originReviewStarRating = myReview.getStarRating();
        Double newReviewStarRating = request.getStarRating();

        given(myBookReviewRepository.findByIdWithMyBookUsingFetchJoin(any())).willReturn(Optional.of(myReview));

        // when
        MyReviewUpdateResponse response = myReviewWriteService.update(request);

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(myReview.getId()),
                () -> assertThat(response.getContent()).isEqualTo(myReview.getContent()),
                () -> assertThat(response.getStarRating()).isEqualTo(myReview.getStarRating()),
                () -> assertThat(myReview.getBook().getStarRating())
                        .isEqualTo(originBookStarRating - originReviewStarRating + newReviewStarRating),
                () -> verify(myBookReviewRepository, times(1)).findByIdWithMyBookUsingFetchJoin(any())
        );
    }

    @DisplayName("다른 유저의 마이 리뷰를 수정시, 예외가 발생한다.")
    @Test
    void occurExceptionWhenUpdateOtherBookReview() {

        // given
        MyReview myReview = MyReviewFixture.COMMON_MY_BOOK_REVIEW.getMyBookReviewBuilder()
                .myBook(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook()).build();
        MyReviewUpdateServiceRequest request = MyReviewDtoTestData.createMyReviewUpdateServiceRequest(
                "OTHER_LOGIN_ID", myReview.getId());

        given(myBookReviewRepository.findByIdWithMyBookUsingFetchJoin(any())).willReturn(Optional.of(myReview));

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> myReviewWriteService.update(request)).isInstanceOf(MyReviewAccessDeniedException.class),
                () -> verify(myBookReviewRepository, times(1)).findByIdWithMyBookUsingFetchJoin(any()),
                () -> assertThat(myReview.getContent()).isNotEqualTo(request.getContent()),
                () -> assertThat(myReview.getStarRating()).isNotEqualTo(request.getStarRating())
        );
    }

    @DisplayName("마이 리뷰를 삭제한다. 삭제시 Book의 별점과 리뷰 수가 감소한다.")
    @Test
    void deleteMyReview() {

        // given
        MyReview myReview = MyReviewFixture.COMMON_MY_BOOK_REVIEW.getMyBookReviewBuilder()
                .book(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook().getBook())
                .myBook(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook()).build();

        MyReviewDeleteServiceRequest request = MyReviewDtoTestData.createMyReviewDeleteServiceRequest(
                myReview.getMyBook().getUserId(), myReview.getId());

        Double originBookStarRating = myReview.getBook().getStarRating();
        Double originReviewStarRating = myReview.getStarRating();
        Integer originReviewCount = myReview.getBook().getReviewCount();

        given(myBookReviewRepository.findByIdWithMyBookUsingFetchJoin(any())).willReturn(Optional.of(myReview));

        // when
        myReviewWriteService.delete(request);

        // then
        assertAll(
                () -> assertThat(myReview.isDeleted()).isTrue(),
                () -> assertThat(myReview.getBook().getStarRating()).isEqualTo(originBookStarRating - originReviewStarRating),
                () -> assertThat(myReview.getBook().getReviewCount()).isEqualTo(originReviewCount - 1),
                () -> verify(myBookReviewRepository, times(1)).findByIdWithMyBookUsingFetchJoin(any())
        );
    }

    @DisplayName("다른 유저의 마이 리뷰를 삭제시, 예외가 발생한다.")
    @Test
    void occurExceptionWhenDeleteOtherBookReview() {

        // given
        MyReview myReview = MyReviewFixture.COMMON_MY_BOOK_REVIEW.getMyBookReviewBuilder()
                .myBook(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook()).build();

        MyReviewDeleteServiceRequest request = MyReviewDtoTestData.createMyReviewDeleteServiceRequest(
                "OTHER_USER_ID", myReview.getId());

        given(myBookReviewRepository.findByIdWithMyBookUsingFetchJoin(any())).willReturn(Optional.of(myReview));

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> myReviewWriteService.delete(request)).isInstanceOf(MyReviewAccessDeniedException.class),
                () -> verify(myBookReviewRepository, times(1)).findByIdWithMyBookUsingFetchJoin(any()),
                () -> assertThat(myReview.isDeleted()).isFalse()
        );
    }
}