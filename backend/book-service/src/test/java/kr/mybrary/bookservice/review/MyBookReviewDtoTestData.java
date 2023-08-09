package kr.mybrary.bookservice.review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse.UserInfoElement;
import kr.mybrary.bookservice.review.domain.dto.request.MyBookReviewCreateServiceRequest;
import kr.mybrary.bookservice.review.domain.dto.request.ReviewsOfBookGetServiceRequest;
import kr.mybrary.bookservice.review.domain.dto.request.ReviewsOfBookGetServiceRequest.ReviewsOfBookGetServiceRequestBuilder;
import kr.mybrary.bookservice.review.persistence.dto.MyBookReviewElementDto;
import kr.mybrary.bookservice.review.presentation.dto.request.MyBookReviewCreateRequest;
import kr.mybrary.bookservice.review.presentation.dto.response.ReviewsOfBookGetResponse;
import kr.mybrary.bookservice.review.presentation.dto.response.ReviewsOfBookGetResponse.ReviewElement;

public class MyBookReviewDtoTestData {

    public static MyBookReviewCreateServiceRequest createMyBookReviewCreateServiceRequest() {
        return MyBookReviewCreateServiceRequest.builder()
                .myBookId(1L)
                .loginId("LOGIN_USER_ID")
                .content("리뷰 내용입니다.")
                .starRating(4.5)
                .build();
    }

    public static MyBookReviewCreateRequest createMyBookReviewCreateRequest() {
        return MyBookReviewCreateRequest.builder()
                .content("리뷰 내용입니다.")
                .starRating(4.5)
                .build();
    }

    public static ReviewsOfBookGetServiceRequestBuilder createReviewOfBookGetServiceRequest() {
        return ReviewsOfBookGetServiceRequest.builder()
                .isbn13("ISBN13");
    }

    public static List<MyBookReviewElementDto> createMyBookReviewElementDtoList() {

        return IntStream.range(1, 6)
                .mapToObj(i -> MyBookReviewElementDto.builder()
                        .id((long) i)
                        .userId("USER_ID_" + i)
                        .content("리뷰_내용_" + i)
                        .starRating((double) i)
                        .createdAt(LocalDateTime.of(2023, 1, 1, i, 0, 0))
                        .build())
                .toList();
    }

    public static UserInfoServiceResponse createUserInfoResponseList() {

        List<UserInfoElement> list = IntStream.range(1, 6)
                .mapToObj(i -> UserInfoElement.builder()
                        .userId("USER_ID_" + i)
                        .nickname("USER_NICKNAME_" + i)
                        .profileImageUrl("USER_PICTURE_URL_" + i)
                        .build())
                .toList();

        return UserInfoServiceResponse.builder()
                .userInfoElements(list)
                .build();
    }

    public static ReviewsOfBookGetResponse createReviewsOfBookGetResponse() {

        return ReviewsOfBookGetResponse.builder()
                .title("Test Book Title")
                .reviewCount(10)
                .starRatingAverage(4.5)
                .isbn13("9788956609959")
                .myBookReviewList(List.of(
                        ReviewElement.builder()
                                .id(1L)
                                .userId("USER_ID_1")
                                .userNickname("USER_NICKNAME_1")
                                .userPictureUrl("USER_PICTURE_URL_1")
                                .content("리뷰_내용_1")
                                .starRating(1.0)
                                .createdAt("2023-01-01")
                                .build(),
                        ReviewElement.builder()
                                .id(1L)
                                .userId("USER_ID_2")
                                .userNickname("USER_NICKNAME_2")
                                .userPictureUrl("USER_PICTURE_URL_2")
                                .content("리뷰_내용_2")
                                .starRating(3.0)
                                .createdAt("2023-01-02")
                                .build()
                ))
                .build();
    }
}
