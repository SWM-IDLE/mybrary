package kr.mybrary.bookservice.mybook.presentation.dto.response;

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
    private String startDateOfPossession;

    private MeaningTag meaningTag;
    private BookDetailResponse book;

    @Getter
    @Builder
    public static class BookDetailResponse {
        private Long id;
        private String isbn13;
        private String title;
        private String description;
        private List<String> authors;
        private List<String> translators;
        private String publisher;
        private String thumbnailUrl;
        private Double starRating;
    }

    @Getter
    @Builder
    public static class MeaningTag {
        private String quote;
        private String colorCode;
    }
}
