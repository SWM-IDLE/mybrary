package kr.mybrary.bookservice.review;

import java.util.List;
import java.util.stream.IntStream;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse.UserInfoElement;
import kr.mybrary.bookservice.review.domain.dto.request.MyBookReviewCreateServiceRequest;
import kr.mybrary.bookservice.review.domain.dto.request.ReviewsOfBookGetServiceRequest;
import kr.mybrary.bookservice.review.persistence.dto.MyBookReviewElementDto;
import kr.mybrary.bookservice.review.presentation.dto.request.MyBookReviewCreateRequest;

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

    public static ReviewOfBookGetServiceRequestBuilder createReviewOfBookGetServiceRequest() {
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
                        .createdAt(null)
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
}
