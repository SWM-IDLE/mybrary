package kr.mybrary.bookservice.mybook.presentation.dto.response;

import java.time.LocalDateTime;
import kr.mybrary.bookservice.mybook.persistence.ReadStatus;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class MyBookElementResponse {

    private Long id;
    private boolean isPublic;
    private boolean isExchangeable;
    private boolean isShareable;
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
        private Integer stars;
    }
}
