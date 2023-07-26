package kr.mybrary.bookservice.booksearch.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoServiceRequest {

    private String keyword;
    private String sort;
    private int page;

    public static KakaoServiceRequest of(String keyword, String sort, int page) {
        return KakaoServiceRequest.builder()
                .keyword(keyword)
                .sort(sort)
                .page(page)
                .build();
    }

    public static KakaoServiceRequest of(String isbn) {
        return KakaoServiceRequest.builder()
                .keyword(isbn)
                .sort("accuracy")
                .page(1)
                .build();
    }
}
