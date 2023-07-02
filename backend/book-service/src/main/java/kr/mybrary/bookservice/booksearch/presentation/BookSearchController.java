package kr.mybrary.bookservice.booksearch.presentation;

import kr.mybrary.bookservice.booksearch.domain.PlatformBookSearchApiService;
import kr.mybrary.bookservice.global.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookSearchController {

    private final PlatformBookSearchApiService bookService;

    @GetMapping("/search")
    public ResponseEntity searchWithKeyword(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "sort", required = false, defaultValue = "accuracy") String sort,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.of(HttpStatus.OK.toString(), "키워드 검색에 성공했습니다.", bookService.searchWithKeyword(keyword, sort, page)));
    }

    @GetMapping("/search/isbn")
    public ResponseEntity searchWithISBNBarcodeScan(
            @RequestParam("isbn") String isbn) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.of(HttpStatus.OK.toString(), "ISBN 검색에 성공했습니다.", bookService.searchWithISBN(isbn)));
    }
}