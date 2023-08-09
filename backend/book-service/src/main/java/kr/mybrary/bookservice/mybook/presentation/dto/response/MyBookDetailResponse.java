package kr.mybrary.bookservice.mybook.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.mybook.persistence.ReadStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookDetailResponse {
    private Long id;
    private boolean showable;
    private boolean exchangeable;
    private boolean shareable;
    private ReadStatus readStatus;
    private LocalDateTime startDateOfPossession;

    private MeaningTag meaningTag;
    private BookDetailResponse book;
    private ReviewResponse review;

    @Getter
    @Builder
    public static class BookDetailResponse {
        private Long id;
        private String title;
        private String description;
        private List<String> authors;
        private List<String> translators;
        private String publisher;
        private String thumbnailUrl;
        private Double stars;
    }

    @Getter
    @Builder
    public static class MeaningTag {
        private String quote;
        private String colorCode;
    }

    @Getter
    @Builder
    public static class ReviewResponse {
        private Long id;
        private String content;
        private Double starRating;
        private String createdAt;
    }
}
