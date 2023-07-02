package kr.mybrary.bookservice.book.presentation;

import kr.mybrary.bookservice.book.domain.BookService;
import kr.mybrary.bookservice.book.presentation.dto.request.BookCreateRequest;
import kr.mybrary.bookservice.global.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity create(@RequestBody BookCreateRequest request) {
        bookService.getRegisteredBook(request.toServiceRequest());

        return ResponseEntity.status(201).body(
                SuccessResponse.of(HttpStatus.CREATED.toString(), "도서 등록에 성공했습니다.", null));
    }
}
