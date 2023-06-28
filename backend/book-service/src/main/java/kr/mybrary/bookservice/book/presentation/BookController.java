package kr.mybrary.bookservice.book.presentation;

import java.util.List;
import kr.mybrary.bookservice.book.domain.PlatformBookSearchApiService;
import kr.mybrary.bookservice.book.presentation.dto.response.BookSearchResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final PlatformBookSearchApiService bookService;

    @GetMapping("/search")
    public ResponseEntity<List<BookSearchResultResponse>> searchWithKeyword(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "sort", required = false, defaultValue = "accuracy") String sort,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        return ResponseEntity.ok(bookService.searchWithKeyword(keyword, sort, page));
    }

    @GetMapping("/search/isbn")
    public ResponseEntity<List<BookSearchResultResponse>> searchWithISBNBarcodeScan(
            @RequestParam("isbn") String isbn) {
        return ResponseEntity.ok(bookService.searchWithISBN(isbn));
    }
}