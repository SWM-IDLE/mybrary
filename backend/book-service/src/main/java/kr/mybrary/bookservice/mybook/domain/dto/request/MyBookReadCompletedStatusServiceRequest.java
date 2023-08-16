package kr.mybrary.bookservice.mybook.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookReadCompletedStatusServiceRequest {

    private String loginId;
    private String isbn13;

    public static MyBookReadCompletedStatusServiceRequest of(String loginId, String isbn13) {
        return MyBookReadCompletedStatusServiceRequest.builder()
                .loginId(loginId)
                .isbn13(isbn13)
                .build();
    }
}
