package kr.mybrary.bookservice.booksearch.domain.dto.response.kakaoapi;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoBookSearchResponse {

    private Meta meta;
    private List<Document> documents;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Document {

        private String title;
        private String contents;
        private String url;
        private String isbn;
        private String datetime;
        private List<String> authors;
        private String publisher;
        private List<String> translators;
        private Integer price;
        private Integer sale_price;
        private String thumbnail;
        private String status;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {

        private Integer total_count;
        private Integer pageable_count;
        private Boolean is_end;
    }
}
