package kr.mybrary.bookservice.book.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookInterestStatusResponse {

    private boolean interested;

    public static BookInterestStatusResponse of(boolean interested) {
        return BookInterestStatusResponse.builder()
                .interested(interested)
                .build();
    }
}
