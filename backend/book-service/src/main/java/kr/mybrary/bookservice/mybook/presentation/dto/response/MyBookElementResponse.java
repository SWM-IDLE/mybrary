package kr.mybrary.bookservice.mybook.presentation.dto.response;

import java.time.LocalDateTime;
import kr.mybrary.bookservice.mybook.persistence.ReadStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyBookElementResponse {

    private Long id;
    private boolean showable;
    private boolean exchangeable;
    private boolean shareable;
    private ReadStatus readStatus;
    private LocalDateTime startDateOfPossession;

    private BookElementResponse book;

    @Getter
    @Setter
    @Builder
    public static class BookElementResponse {
        private Long id;
        private String title;
        private String description;
        private String thumbnailUrl;
        private Double stars;
    }
}
