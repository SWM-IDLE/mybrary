package kr.mybrary.bookservice.mybook.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.mybook.persistence.ReadStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyBookDetailResponse {
    private Long id;
    private boolean isPublic;
    private boolean isExchangeable;
    private boolean isShareable;
    private ReadStatus readStatus;
    private LocalDateTime startDateOfPossession;

    private BookDetailResponse book;

    @Getter
    @Setter
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
}
