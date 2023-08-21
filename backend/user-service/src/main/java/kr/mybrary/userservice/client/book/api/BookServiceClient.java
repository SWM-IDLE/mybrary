package kr.mybrary.userservice.client.book.api;

import kr.mybrary.userservice.client.book.dto.response.BookRecommendationsServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "book-service")
public interface BookServiceClient {

    @GetMapping("/api/v1/books/recommendations/{type}/categories/{categoryId}")
    BookRecommendationsServiceResponse getBookListByCategoryId(
            @PathVariable String type,
            @PathVariable int categoryId,
            @RequestParam int page);
}
