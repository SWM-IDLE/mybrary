package kr.mybrary.userservice.client.book.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRecommendationsServiceResponse {

    private Data data;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private List<BookRecommendationsResponseElement> books;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookRecommendationsResponseElement {
        private String thumbnailUrl;
        private String isbn13;
    }
}
