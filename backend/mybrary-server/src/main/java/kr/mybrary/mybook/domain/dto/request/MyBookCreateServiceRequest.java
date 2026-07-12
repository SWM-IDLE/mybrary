package kr.mybrary.mybook.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookCreateServiceRequest {

    private String userId;
    private String isbn13;
}
