package kr.mybrary.review.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.mybrary.book.domain.BookReadService;
import kr.mybrary.book.persistence.Book;
import kr.mybrary.global.util.DateUtils;
import kr.mybrary.mybook.domain.MyBookService;
import kr.mybrary.mybook.persistence.MyBook;
import kr.mybrary.review.domain.dto.MyReviewDtoMapper;
import kr.mybrary.review.domain.dto.request.MyReviewOfMyBookGetServiceRequest;
import kr.mybrary.review.domain.dto.request.MyReviewsOfBookGetServiceRequest;
import kr.mybrary.review.persistence.model.MyReviewElementModel;
import kr.mybrary.review.persistence.repository.MyReviewRepository;
import kr.mybrary.review.presentation.dto.response.MyReviewOfMyBookGetResponse;
import kr.mybrary.review.presentation.dto.response.MyReviewsOfBookGetResponse;
import kr.mybrary.review.presentation.dto.response.MyReviewsOfBookGetResponse.ReviewElement;
import kr.mybrary.user.domain.UserServicePort;
import kr.mybrary.user.domain.dto.request.UserInfoServiceRequest;
import kr.mybrary.user.domain.dto.response.UserInfoServiceResponse;
import kr.mybrary.user.domain.dto.response.UserInfoServiceResponse.UserInfoElement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyReviewReadService {

    private final MyReviewRepository myBookReviewRepository;
    private final BookReadService bookReadService;
    private final MyBookService myBookService;
    private final UserServicePort userServicePort;

    public MyReviewsOfBookGetResponse getReviewsFromBook(MyReviewsOfBookGetServiceRequest request) {

        Book book = bookReadService.getRegisteredBookByISBN13(request.getIsbn13());
        List<MyReviewElementModel> reviewElements = myBookReviewRepository.findReviewsByBook(book);

        UserInfoServiceResponse usersInfo = userServicePort.getUsersInfo(
                UserInfoServiceRequest.builder()
                        .userIds(getUserIdFromMyBookReview(reviewElements))
                        .build());

        Map<String, UserInfoElement> userInfoMap = createUserInfoMapFromResponse(
                usersInfo.getUserInfoElements());

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

    private static List<ReviewElement> createMyBookReviewElements(
            List<MyReviewElementModel> reviewElements,
            Map<String, UserInfoElement> userInfoMap) {

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

    private static Map<String, UserInfoElement> createUserInfoMapFromResponse(
            List<UserInfoElement> userInfoElements) {

        return userInfoElements.stream()
                .collect(Collectors.toConcurrentMap(
                        UserInfoElement::getUserId,
                        element -> element)
                );
    }

    private double getReviewStarRatingAverage(List<MyReviewElementModel> reviewsByBook) {
        return reviewsByBook.stream()
                .mapToDouble(MyReviewElementModel::getStarRating)
                .average().orElseGet(() -> 0.0);
    }

    private List<String> getUserIdFromMyBookReview(List<MyReviewElementModel> reviewsByBook) {
        return reviewsByBook.stream()
                .map(MyReviewElementModel::getUserId)
                .toList();
    }
}
