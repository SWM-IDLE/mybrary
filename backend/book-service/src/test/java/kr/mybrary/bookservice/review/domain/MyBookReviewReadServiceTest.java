package kr.mybrary.bookservice.review.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.domain.BookReadService;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.client.user.api.UserServiceClient;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse;
import kr.mybrary.bookservice.review.MyBookReviewDtoTestData;
import kr.mybrary.bookservice.review.domain.dto.request.ReviewsOfBookGetServiceRequest;
import kr.mybrary.bookservice.review.persistence.dto.MyBookReviewElementDto;
import kr.mybrary.bookservice.review.persistence.repository.MyBookReviewRepository;
import kr.mybrary.bookservice.review.presentation.dto.response.ReviewsOfBookGetResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MyBookReviewReadServiceTest {

    @InjectMocks
    private MyBookReviewReadService myBookReviewReadService;

    @Mock
    private MyBookReviewRepository myBookReviewRepository;

    @Mock
    private BookReadService bookReadService;

    @Mock
    private UserServiceClient userServiceClient;

    @DisplayName("하나의 책에 대한 모든 리뷰를 조회한다.")
    @Test
    void findReviewsFromBook() {

        // given
        Book book = BookFixture.COMMON_BOOK.getBook();
        List<MyBookReviewElementDto> myBookReviewElementDtoList = MyBookReviewDtoTestData.createMyBookReviewElementDtoList();
        UserInfoServiceResponse userInfoServiceResponse = MyBookReviewDtoTestData.createUserInfoResponseList();

        ReviewsOfBookGetServiceRequest serviceRequest = MyBookReviewDtoTestData.createReviewOfBookGetServiceRequest()
                .isbn13(book.getIsbn13()).build();

        given(bookReadService.getRegisteredBookByISBN13(any())).willReturn(book);
        given(myBookReviewRepository.findReviewsByBook(any())).willReturn(myBookReviewElementDtoList);
        given(userServiceClient.getUsersInfo(any())).willReturn(userInfoServiceResponse);

        // when
        ReviewsOfBookGetResponse response = myBookReviewReadService.getReviewsFromBook(serviceRequest);

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
}