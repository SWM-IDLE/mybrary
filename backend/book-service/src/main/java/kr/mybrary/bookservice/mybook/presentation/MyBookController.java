package kr.mybrary.bookservice.mybook.presentation;

import kr.mybrary.bookservice.global.SuccessResponse;
import kr.mybrary.bookservice.mybook.domain.MyBookService;
import kr.mybrary.bookservice.mybook.presentation.dto.request.MyBookCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mybooks")
@RequiredArgsConstructor
public class MyBookController {

    private final MyBookService myBookService;

    @PostMapping
    public ResponseEntity create(@RequestBody MyBookCreateRequest request) {

        String userId = "test1"; // TODO: 임시 회원 ID
        myBookService.create(request.toServiceRequest(userId));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of(HttpStatus.CREATED.toString(), "내 서재에 도서를 등록했습니다.", null));
    }
}
