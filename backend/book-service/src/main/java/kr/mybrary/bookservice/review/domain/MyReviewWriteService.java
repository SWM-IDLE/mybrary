package kr.mybrary.bookservice.review.domain;

import kr.mybrary.bookservice.mybook.domain.MyBookService;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.domain.dto.request.MyReviewCreateServiceRequest;
import kr.mybrary.bookservice.review.domain.exception.MyReviewAccessDeniedException;
import kr.mybrary.bookservice.review.domain.exception.MyReviewAlreadyExistsException;
import kr.mybrary.bookservice.review.persistence.MyReview;
import kr.mybrary.bookservice.review.persistence.repository.MyReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MyReviewWriteService {

    private final MyReviewRepository myBookReviewRepository;
    private final MyBookService myBookService;

    public void create(MyReviewCreateServiceRequest request) {

        MyBook myBook = myBookService.findMyBookByIdWithBook(request.getMyBookId());
        checkIsOwnerSameAsRequester(myBook.getUserId(), request.getLoginId());
        checkMyBookReviewAlreadyRegistered(myBook);

        myBookReviewRepository.save(MyReview.of(myBook, request));
    }

    private void checkIsOwnerSameAsRequester(String myBookOwnerUserId, String loginId) {
        if (!myBookOwnerUserId.equals(loginId)) {
            throw new MyReviewAccessDeniedException();
        }
    }

    private void checkMyBookReviewAlreadyRegistered(MyBook myBook) {
        if (myBookReviewRepository.existsByMyBook(myBook)) {
            throw new MyReviewAlreadyExistsException();
        }
    }
}
