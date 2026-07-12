package kr.mybrary.mybook.domain.dto.request;

import kr.mybrary.mybook.persistence.MyBookOrderType;
import kr.mybrary.mybook.persistence.ReadStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookFindAllServiceRequest {

    private String loginId;
    private String userId;
    private MyBookOrderType myBookOrderType;
    private ReadStatus readStatus;

    public static MyBookFindAllServiceRequest of(String loginId, String userId, MyBookOrderType myBookOrderType, ReadStatus readStatus) {
        return MyBookFindAllServiceRequest.builder()
                .loginId(loginId)
                .userId(userId)
                .myBookOrderType(myBookOrderType)
                .readStatus(readStatus)
                .build();
    }
}
