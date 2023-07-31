package kr.mybrary.bookservice.mybook.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookCreateServiceRequest {

    private String userId;
    private String isbn13;
}
