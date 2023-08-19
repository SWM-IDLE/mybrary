package kr.mybrary.bookservice.review.domain;

import kr.mybrary.bookservice.book.persistence.Book;
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
        Double newStarRating = request.getStarRating();
        addBookReviewCountAndStarRating(myBook.getBook(), newStarRating);
    }


    public MyReviewUpdateResponse update(MyReviewUpdateServiceRequest request) {

        MyReview myReview = getMyReviewById(request.getMyReviewId());
        Double originStarRating = myReview.getStarRating();
        Double newStarRating = request.getStarRating();

        checkIsOwnerSameAsRequester(myReview.getMyBook().getUserId(), request.getLoginId());

        myReview.update(request);
        updateBookStarRating(myReview.getBook(), originStarRating, newStarRating);
        return MyReviewUpdateResponse.of(myReview);
    }


    public void delete(MyReviewDeleteServiceRequest request) {

        MyReview myReview = getMyReviewById(request.getMyReviewId());
        checkIsOwnerSameAsRequester(myReview.getMyBook().getUserId(), request.getLoginId());

        removeBookReviewCountAndStarRating(myReview.getBook(), myReview.getStarRating());
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

    private void addBookReviewCountAndStarRating(Book book, Double newStarRating) {
        book.updateWhenCreateReview(newStarRating);
    }

    private void updateBookStarRating(Book book, Double originStarRating, Double newStarRating) {
        book.updateWhenUpdateReview(originStarRating, newStarRating);
    }

    private static void removeBookReviewCountAndStarRating(Book book, Double originStarRating) {
        book.updateWhenDeleteReview(originStarRating);
    }
}
