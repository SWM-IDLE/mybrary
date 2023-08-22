package kr.mybrary.bookservice.review.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.mybrary.bookservice.book.domain.BookReadService;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.client.user.api.UserServiceClient;
import kr.mybrary.bookservice.client.user.dto.request.UserInfoRequest;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse.UserInfo;
import kr.mybrary.bookservice.global.util.DateUtils;
import kr.mybrary.bookservice.mybook.domain.MyBookService;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.domain.dto.MyReviewDtoMapper;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewOfMyBookGetServiceRequest;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewsOfBookGetServiceRequest;
import kr.mybrary.bookservice.review.persistence.model.MyReviewElementModel;
import kr.mybrary.bookservice.review.persistence.repository.MyReviewRepository;
import kr.mybrary.bookservice.review.presentation.dto.response.MyReviewOfMyBookGetResponse;
import kr.mybrary.bookservice.review.presentation.dto.response.MyReviewsOfBookGetResponse;
import kr.mybrary.bookservice.review.presentation.dto.response.MyReviewsOfBookGetResponse.ReviewElement;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyReviewReadService {

    private final MyReviewRepository myBookReviewRepository;
    private final BookReadService bookReadService;
    private final MyBookService myBookService;
    private final UserServiceClient userServiceClient;

    public MyReviewsOfBookGetResponse getReviewsFromBook(MyReviewsOfBookGetServiceRequest request) {

        Book book = bookReadService.getRegisteredBookByISBN13(request.getIsbn13());
        List<MyReviewElementModel> reviewElements = myBookReviewRepository.findReviewsByBook(book);

        UserInfoServiceResponse usersInfo = userServiceClient.getUsersInfo(
                UserInfoRequest.of(getUserIdFromMyBookReview(reviewElements)));

        Map<String, UserInfo> userInfoMap = createUserInfoMapFromResponse(usersInfo.getData().getUserInfoElements());

        List<ReviewElement> myBookReviewElements = createMyBookReviewElements(reviewElements, userInfoMap);
        double starRatingAverage = getReviewStarRatingAverage(reviewElements);

        return MyReviewsOfBookGetResponse.builder()
                .title(book.getTitle())
                .isbn13(book.getIsbn13())
                .reviewCount(book.getReviewCount())
                .starRatingAverage(starRatingAverage)
                .myBookReviewList(myBookReviewElements)
                .build();
    }

    public MyReviewOfMyBookGetResponse getReviewFromMyBook(MyReviewOfMyBookGetServiceRequest request) {

        MyBook myBook = myBookService.findMyBookById(request.getMyBookId());

        return myBookReviewRepository.findReviewByMyBook(myBook)
                .map(MyReviewDtoMapper.INSTANCE::reviewOfMyBookModelToResponse)
                .orElseGet(() -> null);
    }

    @NotNull
    private static List<ReviewElement> createMyBookReviewElements(
            List<MyReviewElementModel> reviewElements,
            Map<String, UserInfo> userInfoMap) {

        return reviewElements.stream()
                .filter(review -> userInfoMap.containsKey(review.getUserId()))
                .map(review -> ReviewElement.builder()
                        .id(review.getId())
                        .starRating(review.getStarRating())
                        .createdAt(DateUtils.toHyphenFormatYYYMMddHHmm(review.getCreatedAt()))
                        .content(review.getContent())
                        .userId(review.getUserId())
                        .userNickname(userInfoMap.get(review.getUserId()).getNickname())
                        .userPictureUrl(userInfoMap.get(review.getUserId()).getProfileImageUrl())
                        .build())
                .toList();
    }

    @NotNull
    private static Map<String, UserInfo> createUserInfoMapFromResponse(
            List<UserInfo> userInfoServiceResponses) {

        return userInfoServiceResponses.stream()
                .collect(Collectors.toConcurrentMap(
                        UserInfo::getUserId,
                        userInfoServiceResponse -> userInfoServiceResponse)
                );
    }

    private double getReviewStarRatingAverage(List<MyReviewElementModel> reviewsByBook) {
        return reviewsByBook.stream()
                .mapToDouble(MyReviewElementModel::getStarRating)
                .average().orElseGet(() -> 0.0);
    }

    @NotNull
    private List<String> getUserIdFromMyBookReview(List<MyReviewElementModel> reviewsByBook) {
        return reviewsByBook.stream()
                .map(MyReviewElementModel::getUserId)
                .toList();
    }
}
