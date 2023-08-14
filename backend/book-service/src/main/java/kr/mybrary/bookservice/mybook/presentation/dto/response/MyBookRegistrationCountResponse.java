package kr.mybrary.bookservice.mybook.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookRegistrationCountResponse {

    private Long count;

    public static MyBookRegistrationCountResponse of(Long count) {
        return MyBookRegistrationCountResponse.builder()
                .count(count)
                .build();
    }
}
