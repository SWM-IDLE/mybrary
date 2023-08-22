package kr.mybrary.bookservice.review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse.UserInfo;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewCreateServiceRequest;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewDeleteServiceRequest;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewOfMyBookGetServiceRequest;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewUpdateServiceRequest;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewsOfBookGetServiceRequest;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewsOfBookGetServiceRequest.MyReviewsOfBookGetServiceRequestBuilder;
import kr.mybrary.bookservice.review.persistence.model.MyReviewElementModel;
import kr.mybrary.bookservice.review.persistence.model.MyReviewFromMyBookModel;
import kr.mybrary.bookservice.review.presentation.dto.request.MyReviewCreateRequest;
import kr.mybrary.bookservice.review.presentation.dto.request.MyReviewUpdateRequest;
import kr.mybrary.bookservice.review.presentation.dto.response.MyReviewOfMyBookGetResponse;
import kr.mybrary.bookservice.review.presentation.dto.response.MyReviewUpdateResponse;
import kr.mybrary.bookservice.review.presentation.dto.response.MyReviewsOfBookGetResponse;
import kr.mybrary.bookservice.review.presentation.dto.response.MyReviewsOfBookGetResponse.ReviewElement;

public class MyReviewDtoTestData {

    public static MyReviewCreateServiceRequest createMyBookReviewCreateServiceRequest() {
        return MyReviewCreateServiceRequest.builder()
                .myBookId(1L)
                .loginId("LOGIN_USER_ID")
                .content("리뷰 내용입니다.")
                .starRating(4.5)
                .build();
    }

    public static MyReviewCreateRequest createMyBookReviewCreateRequest() {
        return MyReviewCreateRequest.builder()
                .content("리뷰 내용입니다.")
                .starRating(4.5)
                .build();
    }

    public static MyReviewsOfBookGetServiceRequestBuilder createReviewOfBookGetServiceRequest() {
        return MyReviewsOfBookGetServiceRequest.builder()
                .isbn13("ISBN13");
    }

    public static List<MyReviewElementModel> createMyBookReviewElementDtoList() {

        return IntStream.range(1, 6)
                .mapToObj(i -> MyReviewElementModel.builder()
                        .id((long) i)
                        .userId("USER_ID_" + i)
                        .content("리뷰_내용_" + i)
                        .starRating((double) i)
                        .createdAt(LocalDateTime.of(2023, 1, 1, i, 0, 0))
                        .build())
                .toList();
    }

    public static UserInfoServiceResponse createUserInfoResponseList() {

        List<UserInfo> list = IntStream.range(1, 6)
                .mapToObj(i -> UserInfo.builder()
                        .userId("USER_ID_" + i)
                        .nickname("USER_NICKNAME_" + i)
                        .profileImageUrl("USER_PICTURE_URL_" + i)
                        .build())
                .toList();

        return UserInfoServiceResponse.builder()
                .data(UserInfoServiceResponse.UserInfoList.builder()
                        .userInfoElements(list)
                        .build())
                .build();
    }

    public static MyReviewsOfBookGetResponse createReviewsOfBookGetResponse() {

        return MyReviewsOfBookGetResponse.builder()
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

    public static MyReviewOfMyBookGetServiceRequest createReviewOfMyBookGetServiceRequest() {
        return MyReviewOfMyBookGetServiceRequest.builder()
                .myBookId(1L)
                .build();
    }

    public static MyReviewFromMyBookModel createReviewFromMyBookModel() {
        return MyReviewFromMyBookModel.builder()
                .id(1L)
                .content("리뷰 내용입니다.")
                .starRating(4.5)
                .createdAt(LocalDateTime.of(2023, 1, 1, 0, 0, 0))
                .updatedAt(LocalDateTime.of(2023, 1, 1, 0, 0, 0))
                .build();
    }

    public static MyReviewOfMyBookGetResponse createReviewOfMyBookGetResponse() {
        return MyReviewOfMyBookGetResponse.builder()
                .id(1L)
                .content("리뷰 내용입니다.")
                .starRating(4.5)
                .createdAt("2023.01.01")
                .updatedAt("2023.01.01")
                .build();
    }

    public static MyReviewUpdateServiceRequest createMyReviewUpdateServiceRequest(String loginId, Long myBookId) {
        return MyReviewUpdateServiceRequest.builder()
                .loginId(loginId)
                .myReviewId(myBookId)
                .content("update content")
                .starRating(5.0)
                .build();
    }

    public static MyReviewUpdateRequest createMyReviewUpdateRequest() {
        return MyReviewUpdateRequest.builder()
                .content("update content")
                .starRating(5.0)
                .build();
    }

    public static MyReviewUpdateResponse createMyReviewUpdateResponse() {
        return MyReviewUpdateResponse.builder()
                .id(1L)
                .content("update content")
                .starRating(5.0)
                .build();
    }

    public static MyReviewDeleteServiceRequest createMyReviewDeleteServiceRequest(String loginId, Long myBookId) {
        return MyReviewDeleteServiceRequest.builder()
                .loginId(loginId)
                .myReviewId(myBookId)
                .build();
    }

}
