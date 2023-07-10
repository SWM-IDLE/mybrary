package kr.mybrary.bookservice.mybook.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookDetailServiceRequest {

    private String loginId;
    private Long mybookId;

    public static MyBookDetailServiceRequest of(String loginId, Long mybookId) {
        return MyBookDetailServiceRequest.builder()
                .loginId(loginId)
                .mybookId(mybookId)
                .build();
    }
}
