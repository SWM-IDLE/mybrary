package kr.mybrary.bookservice.review.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.domain.BookReadService;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.client.user.api.UserServiceClient;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse;
import kr.mybrary.bookservice.mybook.MyBookFixture;
import kr.mybrary.bookservice.mybook.domain.MyBookService;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.MyReviewDtoTestData;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewOfMyBookGetServiceRequest;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewsOfBookGetServiceRequest;
import kr.mybrary.bookservice.review.persistence.model.MyReviewFromMyBookModel;
import kr.mybrary.bookservice.review.persistence.model.MyReviewElementModel;
import kr.mybrary.bookservice.review.persistence.repository.MyReviewRepository;
import kr.mybrary.bookservice.review.presentation.dto.response.MyReviewOfMyBookGetResponse;
import kr.mybrary.bookservice.review.presentation.dto.response.MyReviewsOfBookGetResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MyReviewReadServiceTest {

    @InjectMocks
    private MyReviewReadService myReviewReadService;

    @Mock
    private MyReviewRepository myBookReviewRepository;

    @Mock
    private BookReadService bookReadService;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MyBookService myBookService;

    @DisplayName("하나의 책에 대한 모든 리뷰를 조회한다.")
    @Test
    void findReviewsFromBook() {

        // given
        Book book = BookFixture.COMMON_BOOK.getBook();
        List<MyReviewElementModel> myReviewElementModelList = MyReviewDtoTestData.createMyBookReviewElementDtoList();
        UserInfoServiceResponse userInfoServiceResponse = MyReviewDtoTestData.createUserInfoResponseList();

        MyReviewsOfBookGetServiceRequest serviceRequest = MyReviewDtoTestData.createReviewOfBookGetServiceRequest()
                .isbn13(book.getIsbn13()).build();

        given(bookReadService.getRegisteredBookByISBN13(any())).willReturn(book);
        given(myBookReviewRepository.findReviewsByBook(any())).willReturn(myReviewElementModelList);
        given(userServiceClient.getUsersInfo(any())).willReturn(userInfoServiceResponse);

        // when
        MyReviewsOfBookGetResponse response = myReviewReadService.getReviewsFromBook(serviceRequest);

        // then
        assertAll(
                () -> assertThat(response.getTitle()).isEqualTo(book.getTitle()),
                () -> assertThat(response.getIsbn13()).isEqualTo(book.getIsbn13()),
                () -> assertThat(response.getReviewCount()).isEqualTo(book.getReviewCount()),
                () -> assertThat(response.getStarRatingAverage()).isEqualTo(3.0),
                () -> assertThat(response.getMyBookReviewList().size()).isEqualTo(5),
                () -> assertThat(response.getMyBookReviewList()).extracting("userId", "userNickname", "userPictureUrl", "content", "starRating")
                        .contains(tuple("USER_ID_1", "USER_NICKNAME_1", "USER_PICTURE_URL_1", "리뷰_내용_1", 1.0),
                                tuple("USER_ID_2", "USER_NICKNAME_2", "USER_PICTURE_URL_2", "리뷰_내용_2", 2.0),
                                tuple("USER_ID_3", "USER_NICKNAME_3", "USER_PICTURE_URL_3", "리뷰_내용_3", 3.0),
                                tuple("USER_ID_4", "USER_NICKNAME_4", "USER_PICTURE_URL_4", "리뷰_내용_4", 4.0),
                                tuple("USER_ID_5", "USER_NICKNAME_5", "USER_PICTURE_URL_5", "리뷰_내용_5", 5.0))
        );
    }

    @DisplayName("마이북에 대한 리뷰를 조회한다.")
    @Test
    void findReviewFromMyBook() {

        // given
        MyBook myBook = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook();
        MyReviewFromMyBookModel model = MyReviewDtoTestData.createReviewFromMyBookModel();
        MyReviewOfMyBookGetServiceRequest request = MyReviewDtoTestData.createReviewOfMyBookGetServiceRequest();

        given(myBookService.findMyBookById(any())).willReturn(myBook);
        given(myBookReviewRepository.findReviewByMyBook(any())).willReturn(Optional.ofNullable(model));

        // when
        MyReviewOfMyBookGetResponse response = myReviewReadService.getReviewFromMyBook(request);

        // then
        assertAll(
                () -> {
                    assertThat(response).isNotNull();
                    assertThat(model).isNotNull();
                    assertThat(response.getId()).isEqualTo(model.getId());
                    assertThat(response.getContent()).isEqualTo(model.getContent());
                    assertThat(response.getStarRating()).isEqualTo(model.getStarRating());
                },
                () -> verify(myBookReviewRepository, times(1)).findReviewByMyBook(any()),
                () -> verify(myBookService, times(1)).findMyBookById(any())
        );
    }

    @DisplayName("마이북에 대한 리뷰가 없을 경우, 빈 응답을 반환한다.")
    @Test
    void findReviewFromMyBook_Empty() {

        // given
        MyBook myBook = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook();
        MyReviewOfMyBookGetServiceRequest request = MyReviewDtoTestData.createReviewOfMyBookGetServiceRequest();

        given(myBookService.findMyBookById(any())).willReturn(myBook);
        given(myBookReviewRepository.findReviewByMyBook(any())).willReturn(Optional.empty());

        // when
        MyReviewOfMyBookGetResponse response = myReviewReadService.getReviewFromMyBook(request);

        // then
        assertAll(
                () -> assertThat(response).isNull(),
                () -> verify(myBookReviewRepository, times(1)).findReviewByMyBook(any()),
                () -> verify(myBookService, times(1)).findMyBookById(any())
        );
    }
}