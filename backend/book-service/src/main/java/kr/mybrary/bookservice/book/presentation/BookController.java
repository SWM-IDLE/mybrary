package kr.mybrary.bookservice.book.presentation;

import kr.mybrary.bookservice.book.domain.BookService;
import kr.mybrary.bookservice.book.domain.dto.request.BookDetailServiceRequest;
import kr.mybrary.bookservice.book.presentation.dto.request.BookCreateRequest;
import kr.mybrary.bookservice.global.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity create(@RequestBody BookCreateRequest request) {
        bookService.create(request.toServiceRequest());

        return ResponseEntity.status(201).body(
                SuccessResponse.of(HttpStatus.CREATED.toString(), "도서 등록에 성공했습니다.", null));
    }

    @GetMapping("/detail")
    public ResponseEntity getBookDetail(
            @RequestParam(value = "isbn10", required = false, defaultValue = "") String isbn10,
            @RequestParam("isbn13") String isbn13) {

        BookDetailServiceRequest serviceRequest = BookDetailServiceRequest.of(isbn10, isbn13);

        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK.toString(), "도서 상세정보 조회에 성공했습니다.",
                bookService.getBookDetailByISBN(serviceRequest)));
    }
}
