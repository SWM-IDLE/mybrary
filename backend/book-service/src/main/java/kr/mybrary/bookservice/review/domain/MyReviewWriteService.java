package kr.mybrary.bookservice.review.domain;

import kr.mybrary.bookservice.mybook.domain.MyBookService;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewCreateServiceRequest;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewDeleteServiceRequest;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewUpdateServiceRequest;
import kr.mybrary.bookservice.review.domain.exception.MyReviewAccessDeniedException;
import kr.mybrary.bookservice.review.domain.exception.MyReviewAlreadyExistsException;
import kr.mybrary.bookservice.review.domain.exception.MyReviewNotFoundException;
import kr.mybrary.bookservice.review.persistence.MyReview;
import kr.mybrary.bookservice.review.persistence.repository.MyReviewRepository;
import kr.mybrary.bookservice.review.presentation.dto.response.MyReviewUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MyReviewWriteService {

    private final MyReviewRepository myReviewRepository;
    private final MyBookService myBookService;

    public void create(MyReviewCreateServiceRequest request) {

        MyBook myBook = myBookService.findMyBookByIdWithBook(request.getMyBookId());
        checkIsOwnerSameAsRequester(myBook.getUserId(), request.getLoginId());
        checkMyBookReviewAlreadyRegistered(myBook);

        myReviewRepository.save(MyReview.of(myBook, request));
        myBook.getBook().adjustReviewCountAndStarRating(request.getStarRating());
    }

    public MyReviewUpdateResponse update(MyReviewUpdateServiceRequest request) {

        MyReview myReview = getMyReviewById(request.getMyReviewId());
        Double originStarRating = myReview.getStarRating();

        checkIsOwnerSameAsRequester(myReview.getMyBook().getUserId(), request.getLoginId());

        myReview.update(request);
        myReview.getBook().recalculateStarRating(originStarRating, request.getStarRating());
        return MyReviewUpdateResponse.of(myReview);
    }

    public void delete(MyReviewDeleteServiceRequest request) {

        MyReview myReview = getMyReviewById(request.getMyReviewId());
        checkIsOwnerSameAsRequester(myReview.getMyBook().getUserId(), request.getLoginId());

        myReview.getBook().removeReview(myReview.getStarRating());
        myReview.delete();
    }

    private MyReview getMyReviewById(Long myReviewId) {
        return myReviewRepository.findByIdWithMyBookUsingFetchJoin(myReviewId)
                .orElseThrow(MyReviewNotFoundException::new);
    }

    private void checkIsOwnerSameAsRequester(String myBookOwnerUserId, String loginId) {
        if (!myBookOwnerUserId.equals(loginId)) {
            throw new MyReviewAccessDeniedException();
        }
    }

    private void checkMyBookReviewAlreadyRegistered(MyBook myBook) {
        if (myReviewRepository.existsByMyBook(myBook)) {
            throw new MyReviewAlreadyExistsException();
        }
    }
}
