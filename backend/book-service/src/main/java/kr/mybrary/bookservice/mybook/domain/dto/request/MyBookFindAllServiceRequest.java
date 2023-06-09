package kr.mybrary.bookservice.mybook.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookFindAllServiceRequest {

    private String loginId;
    private String userId;

    public static MyBookFindAllServiceRequest of(String loginId, String userId) {
        return MyBookFindAllServiceRequest.builder()
                .loginId(loginId)
                .userId(userId)
                .build();
    }
}
