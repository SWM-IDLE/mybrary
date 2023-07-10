package kr.mybrary.bookservice.mybook.presentation;

import java.util.List;
import kr.mybrary.bookservice.global.dto.response.SuccessResponse;
import kr.mybrary.bookservice.mybook.domain.MyBookService;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookDetailServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookFindAllServiceRequest;
import kr.mybrary.bookservice.mybook.presentation.dto.request.MyBookCreateRequest;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookElementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class MyBookController {

    private final MyBookService myBookService;

    @PostMapping("/mybooks")
    public ResponseEntity createMyBook(@RequestHeader("USER-ID") String loginId, @RequestBody MyBookCreateRequest request) {

        myBookService.create(request.toServiceRequest(loginId));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of(HttpStatus.CREATED.toString(), "내 서재에 도서를 등록했습니다.", null));
    }

    @GetMapping("/users/{userId}/mybooks")
    public ResponseEntity findAllMyBooks(@RequestHeader("USER-ID") String loginId, @PathVariable("userId") String userId) {

        List<MyBookElementResponse> myBooks = myBookService.findAllMyBooks(
                MyBookFindAllServiceRequest.of(userId, loginId));

        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK.toString(), "서재의 도서 목록입니다.", myBooks));
    }

    @GetMapping("/mybooks/{mybookId}")
    public ResponseEntity findMyBookDetail(@RequestHeader("USER-ID") String loginId,
            @PathVariable("mybookId") Long mybookId) {

        MyBookDetailServiceRequest request = MyBookDetailServiceRequest.of(loginId, mybookId);

        return ResponseEntity.ok(
                SuccessResponse.of(HttpStatus.OK.toString(), "마이북 상세보기입니다.", myBookService.findMyBookDetail(request)));
    }

    @DeleteMapping("/mybooks/{id}")
    public ResponseEntity deleteMyBook(@PathVariable Long id) {

        String userId = "test1"; // TODO: 임시 회원 ID

        // TODO
        myBookService.deleteMyBook(userId, id);

        return ResponseEntity.ok(
                SuccessResponse.of(HttpStatus.OK.toString(), "내 서재의 도서를 삭제했습니다.", null));
    }
}
