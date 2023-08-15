package kr.mybrary.bookservice.booksearch.presentation;

import kr.mybrary.bookservice.booksearch.domain.PlatformBookSearchApiService;
import kr.mybrary.bookservice.booksearch.domain.dto.request.BookListByCategorySearchServiceRequest;
import kr.mybrary.bookservice.booksearch.domain.dto.request.BookSearchServiceRequest;
import kr.mybrary.bookservice.global.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookSearchController {

    private final PlatformBookSearchApiService bookService;

    @GetMapping("/search")
    public ResponseEntity searchWithKeyword(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "sort", required = false, defaultValue = "accuracy") String sort,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        BookSearchServiceRequest request = BookSearchServiceRequest.of(keyword, sort, page);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.of(HttpStatus.OK.toString(), "키워드 검색에 성공했습니다.", bookService.searchWithKeyword(request)));
    }

    @GetMapping("/search/detail")
    public ResponseEntity searchBookDetailWithISBN(@RequestParam("isbn") String isbn) {

        BookSearchServiceRequest request = BookSearchServiceRequest.of(isbn);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.of(HttpStatus.OK.toString(), "도서 상세 조회에 성공했습니다.", bookService.searchBookDetailWithISBN(request)));
    }

    @GetMapping("/search/book-list-by-category")
    public ResponseEntity searchBookListByCategory(
            @RequestParam(value = "type") String type,
            @RequestParam(value = "categoryId", required = false, defaultValue = "0") int categoryId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        BookListByCategorySearchServiceRequest request = BookListByCategorySearchServiceRequest.of(type, categoryId, page);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.of(HttpStatus.OK.toString(), "카테고리별 도서 리스트 조회에 성공했습니다.", bookService.searchBookListByCategory(request)));
    }
}