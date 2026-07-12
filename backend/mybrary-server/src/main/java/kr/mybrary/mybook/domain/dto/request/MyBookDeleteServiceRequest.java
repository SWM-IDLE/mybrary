package kr.mybrary.mybook.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookDeleteServiceRequest {

    private String loginId;
    private Long mybookId;

    public static MyBookDeleteServiceRequest of(String loginId, Long mybookId) {
        return MyBookDeleteServiceRequest.builder()
                .loginId(loginId)
                .mybookId(mybookId)
                .build();
    }
}
