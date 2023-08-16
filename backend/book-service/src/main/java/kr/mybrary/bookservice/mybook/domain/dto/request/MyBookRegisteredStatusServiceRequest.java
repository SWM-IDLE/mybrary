package kr.mybrary.bookservice.mybook.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookRegisteredStatusServiceRequest {

    private String loginId;
    private String isbn13;

    public static MyBookRegisteredStatusServiceRequest of(String userId, String isbn13) {
        return MyBookRegisteredStatusServiceRequest.builder()
                .loginId(userId)
                .isbn13(isbn13)
                .build();
    }

}
