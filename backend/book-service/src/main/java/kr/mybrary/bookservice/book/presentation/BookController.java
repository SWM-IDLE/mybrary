package kr.mybrary.bookservice.book.presentation;

import kr.mybrary.bookservice.book.domain.BookService;
import kr.mybrary.bookservice.book.presentation.dto.request.BookCreateRequest;
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
    public ResponseEntity<Void> create(@RequestBody BookCreateRequest request) {
        bookService.getRegisteredOrNewBook(request.toServiceRequest());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
