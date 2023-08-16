package kr.mybrary.bookservice.book.presentation;

import kr.mybrary.bookservice.book.domain.BookInterestService;
import kr.mybrary.bookservice.book.domain.BookReadService;
import kr.mybrary.bookservice.book.domain.BookWriteService;
import kr.mybrary.bookservice.book.domain.dto.request.BookDetailServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.request.BookInterestServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.request.BookInterestStatusServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.request.BookMyInterestFindServiceRequest;
import kr.mybrary.bookservice.book.persistence.BookOrderType;
import kr.mybrary.bookservice.book.presentation.dto.request.BookCreateRequest;
import kr.mybrary.bookservice.global.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookWriteService bookWriteService;
    private final BookReadService bookReadService;
    private final BookInterestService bookInterestService;

    @PostMapping
    public ResponseEntity create(@RequestBody BookCreateRequest request) {
        bookWriteService.create(request.toServiceRequest());

        return ResponseEntity.status(201).body(
                SuccessResponse.of(HttpStatus.CREATED.toString(), "도서 등록에 성공했습니다.", null));
    }

    @GetMapping("/detail")
    public ResponseEntity getBookDetail(
            @RequestParam(value = "isbn10", required = false, defaultValue = "") String isbn10,
            @RequestParam("isbn13") String isbn13,
            @RequestHeader("USER-ID") String loginId) {

        BookDetailServiceRequest serviceRequest = BookDetailServiceRequest.of(loginId, isbn10, isbn13);

        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK.toString(), "도서 상세정보 조회에 성공했습니다.",
                bookReadService.getBookDetailByISBN(serviceRequest)));
    }

    @PostMapping("/{isbn13}/interest")
    public ResponseEntity handleBookInterest(
            @PathVariable("isbn13") String isbn13,
            @RequestHeader("USER-ID") String loginId) {

        BookInterestServiceRequest serviceRequest = BookInterestServiceRequest.of(isbn13, loginId);

        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK.toString(), "관심 도서 처리에 성공했습니다.",
                bookInterestService.handleBookInterest(serviceRequest)));
    }

    @GetMapping("/users/{userId}/interest")
    public ResponseEntity getInterestBooks(
            @PathVariable("userId") String userId,
            @RequestParam(value = "order", required = false, defaultValue = "none") String order) {

        BookMyInterestFindServiceRequest serviceRequest = BookMyInterestFindServiceRequest.of(userId, BookOrderType.of(order));

        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK.toString(), "관심 도서 목록 조회에 성공했습니다.",
                bookInterestService.getBookInterestList(serviceRequest)));
    }

    @GetMapping("/{isbn13}/interest-status")
    public ResponseEntity getInterestStatus(
            @PathVariable("isbn13") String isbn13,
            @RequestHeader("USER-ID") String loginId) {

        BookInterestStatusServiceRequest serviceRequest = BookInterestStatusServiceRequest.of(loginId, isbn13);

        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK.toString(), "관심 도서 상태 조회에 성공했습니다.",
                bookInterestService.getInterestStatus(serviceRequest)));
    }
}
