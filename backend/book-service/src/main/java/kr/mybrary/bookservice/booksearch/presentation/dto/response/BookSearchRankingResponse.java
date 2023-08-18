package kr.mybrary.bookservice.booksearch.presentation.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookSearchRankingResponse {

    private List<BookSearchRanking> books;

    @Getter
    @Builder
    public static class BookSearchRanking {

        private String title;
        private double score;

        public static BookSearchRanking of(String title, double score) {
            return BookSearchRanking.builder()
                    .title(title)
                    .score(score)
                    .build();
        }
    }

    public static BookSearchRankingResponse of(List<BookSearchRanking> bookSearchRankings) {
        return BookSearchRankingResponse.builder()
                .books(bookSearchRankings)
                .build();
    }
}
