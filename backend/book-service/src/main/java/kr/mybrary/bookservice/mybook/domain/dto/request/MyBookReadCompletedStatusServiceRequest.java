package kr.mybrary.bookservice.mybook.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookReadCompletedStatusServiceRequest {

    private String loginId;
    private String isbn13;

    public static MyBookReadCompletedStatusServiceRequest of(String userId, String isbn13) {
        return MyBookReadCompletedStatusServiceRequest.builder()
                .loginId(userId)
                .isbn13(isbn13)
                .build();
    }
}
