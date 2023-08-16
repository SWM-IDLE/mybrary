package kr.mybrary.bookservice.mybook.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookRegisteredStatusResponse {

    private boolean registered;

    public static MyBookRegisteredStatusResponse of(boolean registered) {
        return MyBookRegisteredStatusResponse.builder()
                .registered(registered)
                .build();
    }
}
