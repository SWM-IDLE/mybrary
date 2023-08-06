package kr.mybrary.bookservice.review.domain;

import kr.mybrary.bookservice.mybook.domain.MyBookService;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.domain.dto.request.MyBookReviewCreateServiceRequest;
import kr.mybrary.bookservice.review.domain.exception.MyBookReviewAlreadyExistsException;
import kr.mybrary.bookservice.review.persistence.MyBookReview;
import kr.mybrary.bookservice.review.persistence.repository.MyBookReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MyBookReviewWriteService {

    private final MyBookReviewRepository myBookReviewRepository;
    private final MyBookService myBookService;

    public void create(MyBookReviewCreateServiceRequest request) {

        MyBook myBook = myBookService.findMyBookByIdWithBook(request.getMyBookId());
        checkMyBookReviewAlreadyRegistered(myBook);

        myBookReviewRepository.save(MyBookReview.of(myBook, request));
    }

    private void checkMyBookReviewAlreadyRegistered(MyBook myBook) {
        if (myBookReviewRepository.existsByMyBookAndBook(myBook, myBook.getBook())) {
            throw new MyBookReviewAlreadyExistsException();
        }
    }
}
