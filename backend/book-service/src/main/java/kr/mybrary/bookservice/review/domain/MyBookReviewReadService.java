package kr.mybrary.bookservice.review.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.mybrary.bookservice.book.domain.BookReadService;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.client.user.api.UserServiceClient;
import kr.mybrary.bookservice.client.user.dto.request.UserInfoRequest;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse.UserInfoElement;
import kr.mybrary.bookservice.global.util.DateUtils;
import kr.mybrary.bookservice.mybook.domain.MyBookService;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.domain.dto.MyBookReviewDtoMapper;
import kr.mybrary.bookservice.review.domain.dto.request.ReviewOfMyBookGetServiceRequest;
import kr.mybrary.bookservice.review.domain.dto.request.ReviewsOfBookGetServiceRequest;
import kr.mybrary.bookservice.review.persistence.model.MyBookReviewElementDto;
import kr.mybrary.bookservice.review.persistence.repository.MyBookReviewRepository;
import kr.mybrary.bookservice.review.presentation.dto.response.ReviewOfMyBookGetResponse;
import kr.mybrary.bookservice.review.presentation.dto.response.ReviewsOfBookGetResponse;
import kr.mybrary.bookservice.review.presentation.dto.response.ReviewsOfBookGetResponse.ReviewElement;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyBookReviewReadService {

    private final MyBookReviewRepository myBookReviewRepository;
    private final BookReadService bookReadService;
    private final MyBookService myBookService;
    private final UserServiceClient userServiceClient;

    public ReviewsOfBookGetResponse getReviewsFromBook(ReviewsOfBookGetServiceRequest request) {

        Book book = bookReadService.getRegisteredBookByISBN13(request.getIsbn13());
        List<MyBookReviewElementDto> reviewElements = myBookReviewRepository.findReviewsByBook(book);

        UserInfoServiceResponse usersInfo = userServiceClient.getUsersInfo(
                UserInfoRequest.of(getUserIdFromMyBookReview(reviewElements)));

        Map<String, UserInfoServiceResponse.UserInfoElement> userInfoMap = createUserInfoMapFromResponse(
                usersInfo.getUserInfoElements());

        List<ReviewElement> myBookReviewElements = createMyBookReviewElements(reviewElements, userInfoMap);
        double starRatingAverage = getReviewStarRatingAverage(reviewElements);

        return ReviewsOfBookGetResponse.builder()
                .title(book.getTitle())
                .isbn13(book.getIsbn13())
                .reviewCount(book.getReviewCount())
                .starRatingAverage(starRatingAverage)
                .myBookReviewList(myBookReviewElements)
                .build();
    }

    public ReviewOfMyBookGetResponse getReviewFromMyBook(ReviewOfMyBookGetServiceRequest request) {

        MyBook myBook = myBookService.findMyBookById(request.getMyBookId());

        return myBookReviewRepository.findReviewByMyBook(myBook)
                .map(MyBookReviewDtoMapper.INSTANCE::reviewOfMyBookModelToResponse)
                .orElseGet(() -> null);
    }

    @NotNull
    private static List<ReviewElement> createMyBookReviewElements(
            List<MyBookReviewElementDto> reviewElements,
            Map<String, UserInfoElement> userInfoMap) {

        return reviewElements.stream()
                .map(reviewElement -> ReviewElement.builder()
                        .id(reviewElement.getId())
                        .starRating(reviewElement.getStarRating())
                        .createdAt(DateUtils.toFormatYYYMMddHHmm(reviewElement.getCreatedAt()))
                        .content(reviewElement.getContent())
                        .userId(reviewElement.getUserId())
                        .userNickname(userInfoMap.get(reviewElement.getUserId()).getNickname())
                        .userPictureUrl(userInfoMap.get(reviewElement.getUserId()).getProfileImageUrl())
                        .build())
                .toList();
    }

    @NotNull
    private static Map<String, UserInfoServiceResponse.UserInfoElement> createUserInfoMapFromResponse(
            List<UserInfoServiceResponse.UserInfoElement> userInfoServiceResponses) {

        return userInfoServiceResponses.stream()
                .collect(Collectors.toMap(
                        UserInfoElement::getUserId,
                        userInfoServiceResponse -> userInfoServiceResponse)
                );
    }

    private double getReviewStarRatingAverage(List<MyBookReviewElementDto> reviewsByBook) {
        return reviewsByBook.stream()
                .mapToDouble(MyBookReviewElementDto::getStarRating)
                .average().orElseGet(() -> 0.0);
    }

    @NotNull
    private List<String> getUserIdFromMyBookReview(List<MyBookReviewElementDto> reviewsByBook) {
        return reviewsByBook.stream()
                .map(MyBookReviewElementDto::getUserId)
                .toList();
    }
}
