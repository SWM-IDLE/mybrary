package kr.mybrary.bookservice.review.presentation;

import kr.mybrary.bookservice.global.dto.response.SuccessResponse;
import kr.mybrary.bookservice.review.domain.MyBookReviewReadService;
import kr.mybrary.bookservice.review.domain.MyBookReviewWriteService;
import kr.mybrary.bookservice.review.domain.dto.request.ReviewsOfBookGetServiceRequest;
import kr.mybrary.bookservice.review.presentation.dto.request.MyBookReviewCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MyBookReviewController {

    private final MyBookReviewWriteService myBookReviewWriteService;
    private final MyBookReviewReadService myBookReviewReadService;

    @PostMapping("/mybooks/{myBookId}/reviews")
    public ResponseEntity create(@RequestHeader("USER-ID") String loginId,
            @PathVariable Long myBookId,
            @RequestBody MyBookReviewCreateRequest request) {

        myBookReviewWriteService.create(request.toServiceRequest(loginId, myBookId));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of(HttpStatus.CREATED.toString(), "마이 리뷰를 작성했습니다.", null));
    }

    @GetMapping("/books/{isbn13}/reviews")
    public ResponseEntity getReviewsFromBook(@PathVariable String isbn13) {

        ReviewsOfBookGetServiceRequest request = ReviewsOfBookGetServiceRequest.of(isbn13);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.of(HttpStatus.OK.toString(), "도서의 리뷰 목록입니다.",
                        myBookReviewReadService.getReviewsFromBook(request)));
    }
}
