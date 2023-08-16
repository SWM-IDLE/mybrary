package kr.mybrary.bookservice.mybook.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookReadCompletedStatusResponse {

    private boolean completed;

    public static MyBookReadCompletedStatusResponse of(boolean completed) {
        return MyBookReadCompletedStatusResponse.builder()
                .completed(completed)
                .build();
    }

}
