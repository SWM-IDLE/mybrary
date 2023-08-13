package kr.mybrary.bookservice.mybook.presentation.dto.response;

import kr.mybrary.bookservice.mybook.persistence.ReadStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookElementFromMeaningTagResponse {
    private Long id;
    private boolean showable;
    private boolean exchangeable;
    private boolean shareable;
    private ReadStatus readStatus;
    private String startDateOfPossession;

    private MyBookElementFromMeaningTagResponse.BookElementResponse book;

    @Getter
    @Builder
    public static class BookElementResponse {
        private Long id;
        private String title;
        private String description;
        private String thumbnailUrl;
        private Double starRating;
        private String publicationDate;
    }
}
