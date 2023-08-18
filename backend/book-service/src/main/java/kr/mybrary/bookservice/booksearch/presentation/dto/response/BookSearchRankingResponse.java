package kr.mybrary.bookservice.booksearch.presentation.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookSearchRankingResponse {

    private List<BookSearchRanking> bookSearchKeywords;

    @Getter
    @Builder
    public static class BookSearchRanking {

        private String keyword;
        private double score;

        public static BookSearchRanking of(String keyword, double score) {
            return BookSearchRanking.builder()
                    .keyword(keyword)
                    .score(score)
                    .build();
        }
    }

    public static BookSearchRankingResponse of(List<BookSearchRanking> bookSearchRankings) {
        return BookSearchRankingResponse.builder()
                .bookSearchKeywords(bookSearchRankings)
                .build();
    }
}
