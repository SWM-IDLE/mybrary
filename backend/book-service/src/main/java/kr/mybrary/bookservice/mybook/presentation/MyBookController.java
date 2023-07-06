package kr.mybrary.bookservice.mybook.presentation;

import kr.mybrary.bookservice.global.dto.response.SuccessResponse;
import kr.mybrary.bookservice.mybook.domain.MyBookService;
import kr.mybrary.bookservice.mybook.presentation.dto.request.MyBookCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mybooks")
@RequiredArgsConstructor
public class MyBookController {

    private final MyBookService myBookService;

    @PostMapping
    public ResponseEntity createMyBook(@RequestBody MyBookCreateRequest request) {

        String userId = "test1"; // TODO: 임시 회원 ID
        myBookService.create(request.toServiceRequest(userId));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of(HttpStatus.CREATED.toString(), "내 서재에 도서를 등록했습니다.", null));
    }

    @GetMapping
    public ResponseEntity findAllMyBooks() {

        String userId = "test1"; // TODO: 임시 회원 ID

        return ResponseEntity.ok(
                SuccessResponse.of(HttpStatus.OK.toString(), "내 서재의 도서 목록입니다.", myBookService.findAllMyBooks(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity findMyBookDetail(@PathVariable Long id) {

        String userId = "test1"; // TODO: 임시 회원 ID

        return ResponseEntity.ok(
                SuccessResponse.of(HttpStatus.OK.toString(), "마이북 상세보기입니다.", myBookService.findMyBookDetail(userId, id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMyBook(@PathVariable Long id) {

        String userId = "test1"; // TODO: 임시 회원 ID

        // TODO
        myBookService.deleteMyBook(userId, id);

        return ResponseEntity.ok(
                SuccessResponse.of(HttpStatus.OK.toString(), "내 서재의 도서를 삭제했습니다.", null));
    }
}
