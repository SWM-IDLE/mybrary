package kr.mybrary.bookservice.mybook.domain.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyBookDetailServiceRequest {
    private String loginId;
    private String userId;
    private Long mybookId;

    public static MyBookDetailServiceRequest of(String loginId, String userId, Long mybookId) {
        return MyBookDetailServiceRequest.builder()
                .loginId(loginId)
                .userId(userId)
                .mybookId(mybookId)
                .build();
    }
}
