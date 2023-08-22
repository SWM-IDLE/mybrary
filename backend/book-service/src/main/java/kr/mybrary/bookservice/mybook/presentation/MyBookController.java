package kr.mybrary.bookservice.mybook.presentation;

import kr.mybrary.bookservice.global.dto.response.SuccessResponse;
import kr.mybrary.bookservice.mybook.domain.MyBookService;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookDeleteServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookDetailServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookFindAllServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookFindByMeaningTagQuoteServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookReadCompletedStatusServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookRegisteredStatusServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MybookUpdateServiceRequest;
import kr.mybrary.bookservice.mybook.persistence.MyBookOrderType;
import kr.mybrary.bookservice.mybook.persistence.ReadStatus;
import kr.mybrary.bookservice.mybook.presentation.dto.request.MyBookCreateRequest;
import kr.mybrary.bookservice.mybook.presentation.dto.request.MyBookUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MyBookController {

    private final MyBookService myBookService;

    @PostMapping("/mybooks")
    public ResponseEntity createMyBook(@RequestHeader("USER-ID") String loginId,
            @RequestBody MyBookCreateRequest request) {

        myBookService.create(request.toServiceRequest(loginId));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of(HttpStatus.CREATED.toString(), "내 서재에 도서를 등록했습니다.", null));
    }

    @GetMapping("/users/{userId}/mybooks")
    public ResponseEntity findAllMyBooks(@RequestHeader("USER-ID") String loginId,
            @PathVariable("userId") String userId,
            @RequestParam(value = "order", required = false, defaultValue = "none") String order,
            @RequestParam(value = "readStatus", required = false) String readStatus) {

        MyBookFindAllServiceRequest request = MyBookFindAllServiceRequest.of(loginId, userId, MyBookOrderType.of(order),
                ReadStatus.of(readStatus));

        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK.toString(), "서재의 도서 목록입니다.",
                myBookService.findAllMyBooks(request)));
    }

    @GetMapping("/mybooks/{mybookId}")
    public ResponseEntity findMyBookDetail(@RequestHeader("USER-ID") String loginId,
            @PathVariable("mybookId") Long mybookId) {

        MyBookDetailServiceRequest request = MyBookDetailServiceRequest.of(loginId, mybookId);

        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK.toString(), "마이북 상세보기입니다.",
                        myBookService.findMyBookDetail(request)));
    }

    @DeleteMapping("/mybooks/{mybookId}")
    public ResponseEntity deleteMyBook(@RequestHeader("USER-ID") String loginId,
            @PathVariable Long mybookId) {

        MyBookDeleteServiceRequest request = MyBookDeleteServiceRequest.of(loginId, mybookId);

        myBookService.deleteMyBook(request);

        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK.toString(), "내 서재의 도서를 삭제했습니다.", null));
    }

    @PutMapping("/mybooks/{mybookId}")
    public ResponseEntity updateMyBookProperties(@RequestHeader("USER-ID") String loginId,
            @PathVariable Long mybookId, @RequestBody MyBookUpdateRequest request) {

        MybookUpdateServiceRequest serviceRequest = request.toServiceRequest(loginId, mybookId);

        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK.toString(), "내 서재의 마이북 속성을 수정했습니다.",
                        myBookService.updateMyBookProperties(serviceRequest)));
    }

    @GetMapping("/mybooks/meaning-tags/{meaningTagQuote}")
    public ResponseEntity findMyBooksByMeaningTag(@RequestHeader("USER-ID") String loginId,
            @PathVariable String meaningTagQuote) {

        MyBookFindByMeaningTagQuoteServiceRequest request = MyBookFindByMeaningTagQuoteServiceRequest.of(loginId, meaningTagQuote);

        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK.toString(), "의미 태그를 통해서 마이북을 조회했습니다.",
                        myBookService.findByMeaningTagQuote(request)));
    }

    @GetMapping("/mybooks/today-registration-count")
    public ResponseEntity getTodayRegistrationCount() {

        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK.toString(), "오늘의 마이북 등록 수입니다.",
                        myBookService.getBookRegistrationCountOfToday()));
    }

    @GetMapping("/books/{isbn13}/mybook-registered-status")
    public ResponseEntity getMyBookRegisteredStatus(@RequestHeader("USER-ID") String loginId,
            @PathVariable String isbn13) {

        MyBookRegisteredStatusServiceRequest serviceRequest = MyBookRegisteredStatusServiceRequest.of(loginId, isbn13);

        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK.toString(), "해당 도서의 마이북 등록 상태 여부 입니다.",
                        myBookService.getMyBookRegisteredStatus(serviceRequest)));
    }

    @GetMapping("/books/{isbn13}/read-complete-status")
    public ResponseEntity getReadCompletedStatus(@RequestHeader("USER-ID") String loginId,
            @PathVariable String isbn13) {

        MyBookReadCompletedStatusServiceRequest serviceRequest = MyBookReadCompletedStatusServiceRequest.of(loginId, isbn13);

        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK.toString(), "해당 도서 완독 여부 입니다.",
                        myBookService.getMyBookReadCompletedStatus(serviceRequest)));
    }
}
